package com.cv.service2.cv

import ai.djl.Application
import ai.djl.inference.Predictor
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.modality.cv.output.DetectedObjects
import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ModelZoo
import ai.djl.training.util.ProgressBar
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

/**
 * https://github.com/deepjavalibrary/djl/blob/master/examples/docs/object_detection.md
 */
@Component
class ObjectDetection {

    private val logger = LoggerFactory.getLogger(ObjectDetection::class.java)

//    private val criteria = getCriteria()
//    private val predictor = ModelZoo.loadModel(criteria).newPredictor()

    private val predictor: Predictor<Image, DetectedObjects> by lazy {
        logger.info(">>>> predictor start")
        val lazyValue = ModelZoo.loadModel(criteria).newPredictor()
        logger.info(">>>> predictor end")
        lazyValue
    }
    private val criteria: Criteria<Image, DetectedObjects> by lazy {
        logger.info(">>>> criteria start")
        val lazyValue = buildCriteria()
        logger.info(">>>> criteria end")
        lazyValue
    }

    fun predict(imagePath: String): String {
        val inputImageFile = Paths.get(imagePath)
        val image = ImageFactory.getInstance().fromFile(inputImageFile)

        logger.info(">>>> prediction 1")
        val detection: DetectedObjects = predictor.predict(image)
        logger.info(">>>> prediction 2")

        val outputImageFileName = "${inputImageFile.fileName}_$OUTPUT_FILE_SUFFIX"
        return saveBoundingBoxImage(image, detection, outputImageFileName)
    }

    private fun buildCriteria(): Criteria<Image, DetectedObjects> {
        return Criteria.builder()
            .optApplication(Application.CV.OBJECT_DETECTION)
            .setTypes(Image::class.java, DetectedObjects::class.java)
            .optFilter(CRITERIA_FILTER_KEY, CRITERIA_FILTER_VALUE)
            .optProgress(ProgressBar())
            .build()
    }

    private fun saveBoundingBoxImage(image: Image, detection: DetectedObjects, outputImageFileName: String): String {
        val outputDir = Paths.get(OUTPUT_FOLDER)
        Files.createDirectories(outputDir)

        image.drawBoundingBoxes(detection)

        val imagePath = outputDir.resolve(outputImageFileName)
        image.save(Files.newOutputStream(imagePath), OUTPUT_IMAGE_TYPE)
        return imagePath.toString()
    }

    companion object {

        private const val OUTPUT_IMAGE_TYPE = "jpg"
        private const val OUTPUT_FILE_SUFFIX = "output.$OUTPUT_IMAGE_TYPE"
        private const val OUTPUT_FOLDER = "build/output"

        private const val CRITERIA_FILTER_KEY = "backbone"
        private const val CRITERIA_FILTER_VALUE = "resnet50"
    }
}

//fun main() {
//    val imagePath = "src/main/resources/images/elephant-car-scratch.jpg"
//    ObjectDetection().predict(imagePath)
//}