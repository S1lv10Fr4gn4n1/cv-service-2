package com.cv.service2.service.storage

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class FileSystemStorageService : StorageService {

    private var tempDirectory: String = Files.createTempDirectory(TEMP_DIR).toFile().absolutePath

    override fun getTempDir(): String {
        return tempDirectory
    }

    override fun delete(filePath: String) {
        Files.deleteIfExists(Paths.get(filePath))
    }

    override fun deleteAll() {
        Files.walk(Paths.get(tempDirectory))
            .map { Files.delete(it) }
    }

    companion object {

        private const val TEMP_DIR = "cv2-files"
    }
}