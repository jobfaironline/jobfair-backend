package org.capstone.job_fair.utils;

import org.capstone.job_fair.constants.DataConstraint;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    public static byte[] convertImage(MultipartFile file) throws IOException {
        return convertImage(file, DataConstraint.Account.IMAGE_TYPE, DataConstraint.Account.WIDTH_FACTOR, DataConstraint.Account.HEIGHT_FACTOR, DataConstraint.Account.IMAGE_EXTENSION_TYPE);
    }

    public static byte[] convertCompanyLogo(MultipartFile file) throws IOException {
        return convertImage(file, DataConstraint.Company.COMPANY_LOGO_TYPE, DataConstraint.Company.WIDTH_FACTOR, DataConstraint.Company.HEIGHT_FACTOR, DataConstraint.Company.COMPANY_LOGO_EXTENSION_TYPE);
    }

    public static byte[] convertImage(MultipartFile file, int imageType, double fWidth, double fHeight, String fileExtensionType) throws IOException {
        BufferedImage dbi = null;

        byte[] bytes = file.getBytes();
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage sbi = ImageIO.read(is);

        if (sbi != null) {
            int dWidth = (int) (sbi.getWidth() * fWidth);
            int dHeight = (int) (sbi.getHeight() * fHeight);
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(dbi, fileExtensionType, baos);
        bytes = baos.toByteArray();
        return bytes;
    }
}
