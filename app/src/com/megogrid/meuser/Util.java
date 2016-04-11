package com.megogrid.meuser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Util {

    public static Bitmap decodeFile(File f, int req_quality) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            // final int REQUIRED_SIZE = 128;
            final int REQUIRED_SIZE = req_quality;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            if (scale >= 2) {
                scale /= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return setImgOrientation(f.getPath(), bitmap,
                    bitmap.getWidth(), bitmap.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.out.println("<<<checking RegUtility.decodeFile() " + e);
        }
        return null;
    }

    public static Bitmap setImgOrientation(String filepath, Bitmap image,
                                           int req_width, int req_height) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(filepath);
            String orientString = exif
                    .getAttribute(ExifInterface.TAG_ORIENTATION);
            int orientation = orientString != null ? Integer
                    .parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    rotationAngle = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotationAngle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotationAngle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotationAngle = 270;
                    break;

                default:
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) image.getWidth() / 2,
                    (float) image.getHeight() / 2);
            image = Bitmap.createBitmap(image, 0, 0, req_width, req_height,
                    matrix, true);

            return image;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
