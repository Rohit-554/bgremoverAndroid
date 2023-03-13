package com.example.bgremove

import android.graphics.*
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class bgremove : AppCompatActivity() {
    private var imageview: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bgremove)
        imageview = findViewById(R.id.test)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.a1)
//        rb(bitmap)
//        rbgreen(bitmap)
//        grayscale(bitmap)
//        rb(bitmap)
        rbexpred(bitmap)
    }



    fun grayscale(bitmap: Bitmap){
        val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
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
        imageview?.setImageBitmap(outputBitmap)
        Toast.makeText(this, "working rb", Toast.LENGTH_SHORT).show()
        return outputBitmap
    }

    fun rbgreen(bitmap: Bitmap): Bitmap {
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
            else{
                pixels[i] = Color.rgb(15*gray,5*gray ,133*gray)
            }
        }

        // Create the output bitmap
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        imageview?.setImageBitmap(outputBitmap)
        Toast.makeText(this, "working rb", Toast.LENGTH_SHORT).show()
        return outputBitmap
    }

    fun rbexpblue(bitmap: Bitmap): Bitmap {
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
        val blueInk = Color.parseColor("#E00F3285")
        for (i in grayPixels.indices) {
            val gray = Color.red(grayPixels[i])
            if (gray > threshold) {
                pixels[i] = Color.TRANSPARENT
            } else {
                pixels[i] = blueInk
            }
        }

        // Create the output bitmap
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        imageview?.setImageBitmap(outputBitmap)
        Toast.makeText(this, "working rb", Toast.LENGTH_SHORT).show()
        imageview?.setImageBitmap(outputBitmap)
        return outputBitmap
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
        imageview?.setImageBitmap(outputBitmap)
        Toast.makeText(this, "working rb", Toast.LENGTH_SHORT).show()
        imageview?.setImageBitmap(outputBitmap)
        return outputBitmap
    }

    fun rbexblack(bitmap: Bitmap): Bitmap {
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
        val blackInk = Color.parseColor("#E0000000")

        for (i in grayPixels.indices) {
            val gray = Color.red(grayPixels[i])
            if (gray > threshold) {
                pixels[i] = Color.TRANSPARENT
            } else {
                pixels[i] = blackInk
            }
        }

        // Create the output bitmap
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        imageview?.setImageBitmap(outputBitmap)
        Toast.makeText(this, "working rb", Toast.LENGTH_SHORT).show()
        imageview?.setImageBitmap(outputBitmap)
        return outputBitmap
    }



}