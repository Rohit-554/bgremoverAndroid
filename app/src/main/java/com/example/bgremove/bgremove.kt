package com.example.bgremove

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.random.Random

class bgremove : AppCompatActivity() {
    private var imageview: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bgremove)
        imageview = findViewById(R.id.test)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ajeeb)
//        rb(bitmap)
//        rbgreen(bitmap)
//        grayscale(bitmap)
//        rb(bitmap)
        rbexpred(bitmap)
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
        rb(grayscaleBitmap)

    }

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
//        imageview?.setImageBitmap(outputBitmap)
        Toast.makeText(this, "working rb", Toast.LENGTH_SHORT).show()
//        distortImage(outputBitmap)

        return outputBitmap
    }

    fun distortImage(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val distortedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val distortionFactor = 0.02f // Adjust the distortion factor as needed
        val xDistortion = (width * distortionFactor).toInt()
        val yDistortion = (height * distortionFactor).toInt()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val distX = x + xDistortion * sin(y.toDouble() / height * 2 * PI).toFloat()
                val distY = y + yDistortion * cos(x.toDouble() / width * 2 * PI).toFloat()
                if (distX >= 0 && distX < width && distY >= 0 && distY < height) {
                    val color = bitmap.getPixel(distX.toInt(), distY.toInt())
                    distortedBitmap.setPixel(x, y, color)
                }
            }
        }

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
        scatterBitmap(outputBitmap,10)

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
        distortImage(outputBitmap)
        // Return the scattered Bitmap
        return outputBitmap
    }


}