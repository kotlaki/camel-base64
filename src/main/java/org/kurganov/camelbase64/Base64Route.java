package org.kurganov.camelbase64;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.Base64DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
    С помощью Java написать программу, реализующую передачу сообщения из одной
     очереди Apache ActiveMQ в другую. Формат исходного сообщения base64.
     Программа должна пересылать сообщение в раскодированном виде.
     Добавить обработку ошибок при получении пустого сообщения в виде вывода произвольного
     текста ошибки в консоль Java. Пустым сообщением считать сообщение, не содержащее ни
     одного символа.
 */

@Component
public class Base64Route extends RouteBuilder {

    @Autowired
    private MyBean myBean;

    @Autowired
    private Decoder decoder;

    @Override
    public void configure() throws Exception {
        // кодируем файл base64 и сохраняем в файл папке encode
        from("file:E:\\work_temp\\project\\camel-base64\\src\\data?noop=true")
                .marshal().base64()
                .to("file:E:\\work_temp\\project\\camel-base64\\src\\encode");

        // берем сообщение формата base64
        from("file:E:\\work_temp\\project\\camel-base64\\src\\encode?noop=true")
                // обрабатываем сообщение на пустоту
                .doTry()
                    .choice()
                    .when(body().isEqualTo(""))
                    .transform().constant("Пустое сообщение!!!")
                    .to("jms:queue:error")
                    .log(LoggingLevel.ERROR, "Пустое сообщение!!!")
                    .endChoice()
                .otherwise()
                    // раскодируем
                    .bean(decoder)
                    // либо сохраняем сообщение в файл
//                       .to("file:E:\work_temp\project\camel-base64\src\decode")
                    // либо помещаем в очередь
                    .to("jms:queue:result")
                .end();

        // Потребитель
        from("jms:queue:result")
                .bean(myBean);
    }
}
