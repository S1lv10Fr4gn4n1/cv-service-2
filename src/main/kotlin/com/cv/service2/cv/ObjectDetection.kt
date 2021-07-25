package com.cv.service2.cv

import ai.djl.Application
import ai.djl.inference.Predictor
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.modality.cv.output.DetectedObjects
import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ModelZoo
import ai.djl.training.util.ProgressBar
import com.cv.service2.LoggerTime
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

/**
 * https://github.com/deepjavalibrary/djl/blob/master/examples/docs/object_detection.md
 */
@Component
class ObjectDetection() {

//    private val criteria = getCriteria()
//    private val predictor = ModelZoo.loadModel(criteria).newPredictor()

    private val predictor: Predictor<Image, DetectedObjects> by lazy {
        LoggerTime.start("predictor")
        val lazyValue = ModelZoo.loadModel(criteria).newPredictor()
        LoggerTime.stop("predictor")
        lazyValue
    }
    private val criteria: Criteria<Image, DetectedObjects> by lazy {
        LoggerTime.start("criteria")
        val lazyValue = buildCriteria()
        LoggerTime.stop("criteria")
        lazyValue
    }

    fun predict(inputImagePath: String, outputImagePath: String) {
        val inputImageFile = Paths.get(inputImagePath)
        val image = ImageFactory.getInstance().fromFile(inputImageFile)

        LoggerTime.start("prediction")
        val detection: DetectedObjects = predictor.predict(image)
        LoggerTime.stop("prediction")

        LoggerTime.start("saveBoundingBoxImage")
        saveBoundingBoxImage(image, detection, outputImagePath)
        LoggerTime.stop("saveBoundingBoxImage")
    }

    private fun buildCriteria(): Criteria<Image, DetectedObjects> {
        return Criteria.builder()
            .optApplication(Application.CV.OBJECT_DETECTION)
            .setTypes(Image::class.java, DetectedObjects::class.java)
            .optFilter(CRITERIA_FILTER_KEY, CRITERIA_FILTER_VALUE)
            .optProgress(ProgressBar())
            .build()
    }

    private fun saveBoundingBoxImage(image: Image, detection: DetectedObjects, outputImagePath: String) {
        // TODO SFS improve it, file management should happen else where
        image.drawBoundingBoxes(detection)
        image.save(Files.newOutputStream(Paths.get(outputImagePath)), OUTPUT_IMAGE_TYPE)
    }

    companion object {

        private const val OUTPUT_IMAGE_TYPE = "jpg"

        private const val CRITERIA_FILTER_KEY = "backbone"
        private const val CRITERIA_FILTER_VALUE = "resnet50"
    }
}

//fun main() {
//    val imagePath = "src/main/resources/images/elephant-car-scratch.jpg"
//    ObjectDetection().predict(imagePath)
//}