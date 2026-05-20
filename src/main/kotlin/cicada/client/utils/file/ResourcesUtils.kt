package cicada.client.utils.file

import cicada.client.CicadaClient
import cicada.client.utils.client.mc
import com.mojang.blaze3d.platform.NativeImage
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier
import java.io.File
import java.io.FileInputStream

// SCWGxD regrets everything he did. 18.05.2026 15:06.
fun loadTexturesFromFolder(folderPath: String, modId: String) {
    val folder = File(folderPath)

    // Проверяем, существует ли папка и является ли она директорией
    if (!folder.exists() || !folder.isDirectory) {
        println("Папка не найдена или не является директорией: ${folder.absolutePath}")
        return
    }

    val textureManager = mc.textureManager

    // Перебираем все файлы в папке
    folder.listFiles()?.forEach { file ->
        // Нас интересуют только файлы с расширением .png
        if (file.isFile && file.extension.lowercase() == "png") {
            try {
                // Читаем файл в NativeImage
                FileInputStream(file).use { inputStream ->
                    val nativeImage = NativeImage.read(inputStream)

                    // Создаем DynamicTexture (которая наследует AbstractTexture)
                    val texture = DynamicTexture({ "${CicadaClient.MOD_ID}/resources" }, nativeImage)

                    // Имя файла без расширения приводим к нижнему регистру для ResourceLocation
                    val textureName = file.nameWithoutExtension.lowercase()

                    // Внимание: для Minecraft 1.21+ используйте ResourceLocation.fromNamespaceAndPath(modId, "external/$textureName")
                    // Для Minecraft 1.20.x и ниже:
                    val resourceLocation = Identifier.fromNamespaceAndPath(modId, "external/$textureName")

                    // Регистрируем текстуру в менеджере текстур
                    textureManager.register(resourceLocation, texture)

                    println("Успешно загружена внешняя текстура: $resourceLocation из файла ${file.name}")
                }
            } catch (e: Exception) {
                println("Ошибка при загрузке текстуры из файла ${file.name}")
                e.printStackTrace()
            }
        }
    }
}