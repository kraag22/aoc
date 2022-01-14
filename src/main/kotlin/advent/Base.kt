package advent

open class Base {
    companion object {
        fun readData(resource: String):List<String> {
            val data = this::class.java.getResource(resource)!!.readText()
            return data.split("\n")
        }
    }
}
