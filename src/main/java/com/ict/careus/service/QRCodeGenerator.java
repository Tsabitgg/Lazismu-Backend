//package com.ict.careus.service;
//
//import net.glxn.qrgen.QRCode;
//import net.glxn.qrgen.image.ImageType;
//
//import java.io.ByteArrayOutputStream;
//
//public class QRCodeGenerator {
//
//    public static byte[] generateQRCode(String vaNumber) {
//        ByteArrayOutputStream stream = QRCode.from(vaNumber).to(ImageType.PNG).stream();
//        return stream.toByteArray();
//    }
//}
