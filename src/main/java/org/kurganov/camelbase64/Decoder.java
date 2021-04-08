package org.kurganov.camelbase64;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
public class Decoder implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        InputStream is = exchange.getIn().getBody(InputStream.class);
        String fileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int pos = -1;
        while ((pos = bufferedInputStream.read(buf)) != -1) {
            byteArrayOutputStream.write(buf, 0, pos);
        }
        bufferedInputStream.close();
        byte[] encodeBytes = Base64.decodeBase64(byteArrayOutputStream.toByteArray());
        exchange.getOut().setHeader(Exchange.FILE_NAME, fileName);
        exchange.getOut().setBody(encodeBytes);
    }
}

//public class Encoder {
//    public static void main(String[] args) {
//        String str = "Hello Base64!";
//        Base64.Encoder encoder = Base64.getEncoder();
//        byte[] encBytes = encoder.encode(str.getBytes());
//        for (int i = 0; i < encBytes.length; i++) {
//            System.out.printf("%c", encBytes[i]);
//            if(i != 0 && i % 4 == 0) {
//                System.out.println(' ');
//            }
//        }
//
//        System.out.println();
//
//        Base64.Decoder decoder = Base64.getDecoder();
//        byte[] decBytes = decoder.decode(encBytes);
//        System.out.println(new String(decBytes));
//    }
//}
