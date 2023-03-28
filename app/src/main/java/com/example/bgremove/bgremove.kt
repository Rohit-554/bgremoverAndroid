package com.example.bgremove

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.slowmac.autobackgroundremover.BackgroundRemover
import com.slowmac.autobackgroundremover.OnBackgroundChangeListener
import kotlin.math.*
import kotlin.random.Random

class bgremove : AppCompatActivity() {
    private var imageview: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bgremove)
        imageview = findViewById(R.id.test)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.rohit)
//        rb(bitmap)
//        rbgreen(bitmap)
//        rbexpred(bitmap)
//        grayscale(bitmap)
//        rb(bitmap)
//        removeBackgroundx(bitmap)

        //GhyasAhmad function Implemntation for removing the backgournd
        BackgroundRemover.bitmapForProcessing(
            bitmap,
            true,
            object: OnBackgroundChangeListener {
                override fun onSuccess(bitmap: Bitmap) {
                    //bitmap
                    imageview?.setImageBitmap(bitmap)
                }

                override fun onFailed(exception: Exception) {
                    //exception
                }
            }
        )
    }

    //get the painted image here
    fun removeBackgroundx(bitmap: Bitmap): Bitmap {
        val threshold = 127
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val grayBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayBitmap)
        val grayPaint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        }
        canvas.drawBitmap(bitmap, 0f, 0f, grayPaint)
        val paint = Paint().apply {
            color = Color.BLACK
        }
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val canvas2 = Canvas(mutableBitmap)
        canvas2.drawRect(rect, paint)
        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val pixel = grayBitmap.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)
                val gray = (red + green + blue) / 3
                if (gray > threshold) {
                    mutableBitmap.setPixel(x, y, Color.TRANSPARENT)
                }
            }
        }
        imageview?.setImageBitmap(mutableBitmap)
        return mutableBitmap
    }


    fun grayscale(bitmap: Bitmap) {
        val grayscaleBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscaleBitmap)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)

        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    //get the painted image here
    fun rb(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val grayPixels = IntArray(pixels.size)

        // Convert the image to grayscale
        for (i in pixels.indices) {
            val r = Color.red(pixels[i])
            val g = Color.green(pixels[i])
            val b = Color.blue(pixels[i])
            val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
            grayPixels[i] = Color.rgb(gray, gray, gray)
        }

        // Compute the threshold using Otsu's method
        val threshold: Int
        val histogram = IntArray(256)
        for (gray in grayPixels) {
            histogram[Color.red(gray)]++
        }
        val total = grayPixels.size
        var sum = 0.0
        for (i in 0 until 256) {
            sum += i * histogram[i].toDouble()
        }
        var sumB = 0.0
        var wB = 0
        var wF = 0
        var varMax = 0.0
        var level = 0
        for (i in 0 until 256) {
            wB += histogram[i]
            if (wB == 0) continue
            wF = total - wB
            if (wF == 0) break
            sumB += i * histogram[i].toDouble()
            val mB = sumB / wB
            val mF = (sum - sumB) / wF
            val varBetween = wB.toDouble() * wF.toDouble() * (mB - mF) * (mB - mF)
            if (varBetween > varMax) {
                varMax = varBetween
                level = i
            }
        }
        threshold = level

        // Apply binary threshold to separate background from foreground objects
        for (i in grayPixels.indices) {
            val gray = Color.red(grayPixels[i])
            if (gray > threshold) {
                pixels[i] = Color.TRANSPARENT
            }
        }

        // Create the output bitmap
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        imageview?.setImageBitmap(outputBitmap)
        Toast.makeText(this, "working rb", Toast.LENGTH_SHORT).show()
//        distortImage(outputBitmap)

        return outputBitmap
    }

    fun distortImage(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        var distX = 0f;
        var distY = 0f;
        val distortedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val distortionFactor = 0.02f // Adjust the distortion factor as needed
        val xDistortion = (width * distortionFactor).toInt()
        val yDistortion = (height * distortionFactor).toInt()

        for (x in 0 until width) {
            for (y in 0 until height) {
                distX = x + xDistortion * sin(y.toDouble() / height * 2 * PI).toFloat()
                distY = y + yDistortion * cos(x.toDouble() / width * 2 * PI).toFloat()
                if (distX >= 0 && distX < width && distY >= 0 && distY < height) {
                    val color = bitmap.getPixel(distX.toInt(), distY.toInt())
                    distortedBitmap.setPixel(x, y, color)
                }
            }
        }
        Log.d("distortX", "$distX,$distY")
//        imageview?.setImageBitmap(distortedBitmap)
        reverseDistortImage(distortedBitmap)
        return distortedBitmap
    }

 //write a new reverse function for distort image
    fun reverseDistortImage(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        var distX = 0f;
        var distY = 0f;
        val distortedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val distortionFactor = 0.02f // Adjust the distortion factor as needed
        val xDistortion = (width * distortionFactor).toInt()
        val yDistortion = (height * distortionFactor).toInt()

        for (x in 0 until width) {
            for (y in 0 until height) {
                distX = x - xDistortion * sin(1 - (y.toDouble() / height * 2 * PI)).toFloat()
                distY = y - yDistortion * cos(x.toDouble() / width * 2 * PI).toFloat()
                if (distX >= 0 && distX < width && distY >= 0 && distY < height) {
                    val color = bitmap.getPixel(distX.toInt(), distY.toInt())
                    distortedBitmap.setPixel(x, y, color)
                }
            }
        }
        Log.d("distortX", "$distX,$distY")
        imageview?.setImageBitmap(distortedBitmap)
        return distortedBitmap
    }






    fun blurImage(bitmap: Bitmap, radius: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val matrixSize = radius * 2 + 1
        val matrix = FloatArray(matrixSize * matrixSize)

        for (i in matrix.indices) {
            matrix[i] = 1f / (matrixSize * matrixSize)
        }

        val blurredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(blurredBitmap)
        val paint = Paint()
        val blurMaskFilter = BlurMaskFilter(radius.toFloat(), BlurMaskFilter.Blur.NORMAL)
        paint.maskFilter = blurMaskFilter

        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        imageview?.setImageBitmap(blurredBitmap)
        return blurredBitmap
    }


    fun rbexpred(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val grayPixels = IntArray(pixels.size)

        // Convert the image to grayscale
        for (i in pixels.indices) {
            val r = Color.red(pixels[i])
            val g = Color.green(pixels[i])
            val b = Color.blue(pixels[i])
            val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
            grayPixels[i] = Color.rgb(gray, gray, gray)
        }

        // Compute the threshold using Otsu's method
        val threshold: Int
        val histogram = IntArray(256)
        for (gray in grayPixels) {
            histogram[Color.red(gray)]++
        }
        val total = grayPixels.size
        var sum = 0.0
        for (i in 0 until 256) {
            sum += i * histogram[i].toDouble()
        }
        var sumB = 0.0
        var wB = 0
        var wF = 0
        var varMax = 0.0
        var level = 0
        for (i in 0 until 256) {
            wB += histogram[i]
            if (wB == 0) continue
            wF = total - wB
            if (wF == 0) break
            sumB += i * histogram[i].toDouble()
            val mB = sumB / wB
            val mF = (sum - sumB) / wF
            val varBetween = wB.toDouble() * wF.toDouble() * (mB - mF) * (mB - mF)
            if (varBetween > varMax) {
                varMax = varBetween
                level = i
            }
        }
        threshold = level

        // Convert grayscale to red
        val RedInk = Color.parseColor("#E0AC3235")

        for (i in grayPixels.indices) {
            val gray = Color.red(grayPixels[i])
            if (gray > threshold) {
                pixels[i] = Color.TRANSPARENT
            } else {
                pixels[i] = RedInk
            }
        }

        // Create the output bitmap
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
//        distortImage(outputBitmap)
        imageview?.setImageBitmap(outputBitmap)
        return outputBitmap
    }
    fun scatterBitmap(bitmap: Bitmap, amount: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // Create a new Bitmap to hold the output image
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Iterate over all pixels in the input Bitmap
        for (y in 0 until height) {
            for (x in 0 until width) {
                // Calculate a random offset for this pixel
                val xOffset = (Random.nextInt(amount) * if (Random.nextBoolean()) 1 else -1).toFloat()
                val yOffset = (Random.nextInt(amount) * if (Random.nextBoolean()) 1 else -1).toFloat()

                // Calculate the new position of the pixel
                val newX = x + xOffset
                val newY = y + yOffset

                // Ensure the new position is within the bounds of the image
                val clampedX = newX.coerceIn(0.0f, width - 1.0f)
                val clampedY = newY.coerceIn(0.0f, height - 1.0f)

                // Get the color of the pixel at the new position
                val color = bitmap.getPixel(clampedX.toInt(), clampedY.toInt())

                // Set the color of the output pixel
                outputBitmap.setPixel(x, y, color)
            }
        }

        // Return the scattered Bitmap
        return outputBitmap
    }

    fun removeBackground(bitmap: Bitmap): Bitmap {
        // Convert bitmap to mutable bitmap
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // Define the color to replace the background with
        val backgroundColor = Color.TRANSPARENT

        // Define the threshold for color similarity
        val colorTolerance = 50

        // Loop through every pixel in the image
        for (x in 0 until mutableBitmap.width) {
            for (y in 0 until mutableBitmap.height) {

                // Get the color of the current pixel
                val pixelColor = mutableBitmap.getPixel(x, y)

                // Check if the color is similar to the background color
                if (Color.red(pixelColor) < colorTolerance && Color.green(pixelColor) < colorTolerance && Color.blue(pixelColor) < colorTolerance) {
                    mutableBitmap.setPixel(x, y, backgroundColor)
                }
            }
        }

        // Return the modified bitmap
        imageview?.setImageBitmap(mutableBitmap)
        return mutableBitmap
    }


}