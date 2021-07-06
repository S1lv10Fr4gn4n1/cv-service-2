package com.cv.service2.cv

import ai.djl.Application
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.modality.cv.output.DetectedObjects
import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ModelZoo
import ai.djl.training.util.ProgressBar
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

/**
 * https://github.com/deepjavalibrary/djl/blob/master/examples/docs/object_detection.md
 */
@Component
class ObjectDetection {

    private val criteria = getCriteria()
    private val predictor = ModelZoo.loadModel(criteria).newPredictor()

    // TODO SFS, probably the image path will come from S3
    fun predict(imagePath: String): String {
        val imageFile = Paths.get(imagePath)
        val image = ImageFactory.getInstance().fromFile(imageFile)

        val detection: DetectedObjects = predictor.predict(image)
        return saveBoundingBoxImage(image, detection)
    }

    private fun getCriteria(): Criteria<Image, DetectedObjects>? {
        return Criteria.builder()
            .optApplication(Application.CV.OBJECT_DETECTION)
            .setTypes(Image::class.java, DetectedObjects::class.java)
            .optFilter("backbone", "resnet50")
            .optProgress(ProgressBar())
            .build()
    }

    private fun saveBoundingBoxImage(image: Image, detection: DetectedObjects): String {
        val outputDir = Paths.get("build/output")
        Files.createDirectories(outputDir)

        val newImage = image.duplicate(Image.Type.TYPE_INT_ARGB)
        newImage.drawBoundingBoxes(detection)

        val imagePath = outputDir.resolve("detected-output.png")
        newImage.save(Files.newOutputStream(imagePath), "png")
        return imagePath.toString()
    }
}

fun main() {
    val imagePath = "src/main/resources/images/elephant-car-scratch.jpg"
    ObjectDetection().predict(imagePath)
}