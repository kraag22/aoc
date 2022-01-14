package advent

open class Base {
    companion object {
        fun readData(filename: String):List<String> {
            try {
                val resourceFile = this::class.java.getResource(filename)
                if (resourceFile != null) {
                    return resourceFile.readText().split("\n")
                } else {
                    println("Resource file $filename doesn't exists")
                    throw(Exception("File not found"))
                }

            } catch (e: Exception) {
                println("Error while reading $filename resource $e")
            }
            return emptyList()
        }
    }
}
