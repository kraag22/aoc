package advent

class Second: Base() {
    var commands = listOf<Command>()

    fun loadFile(resource: String) {
        loadData(readData(resource))
    }

    fun loadData(lines: List<String>) {
        commands = lines.map {
            val args = it.split(' ')
            Command.from(args[0], args[1].toInt())
        }
    }

    fun computeSimple():Int {
        var x = 0
        var y = 0

        commands.forEach { command ->
            x += command.getXdelta()
            y += command.getYdelta()
        }

        return x * y
    }

    fun computeWithAim(): Int {
        var x = 0
        var y = 0
        var aim = 0

        commands.forEach { command ->
            x += command.getXdelta(aim)
            y += command.getYdelta(aim)
            aim += command.getAimDelta(aim)
        }

        return x * y
    }
}

sealed class Command(val value: Int) {
    abstract fun getXdelta(): Int
    abstract fun getYdelta(): Int

    abstract fun getXdelta(aim: Int): Int
    abstract fun getYdelta(aim: Int): Int
    abstract fun getAimDelta(aim: Int): Int

    companion object {
        fun from(cmd: String, value: Int): Command {
            return when(cmd) {
                "forward" -> ForwardCommand(value)
                "up" -> UpCommand(value)
                "down" -> DownCommand(value)
                else -> throw Exception("Unknown command: $cmd")
            }
        }
    }
}

class ForwardCommand(value:Int): Command(value) {
    override fun getXdelta(): Int {
        return value
    }

    override fun getXdelta(aim: Int): Int {
        return value
    }

    override fun getYdelta(): Int {
        return 0
    }

    override fun getYdelta(aim: Int): Int {
        return value * aim
    }

    override fun getAimDelta(aim: Int): Int {
        return 0
    }
}
class UpCommand(value:Int): Command(value) {
    override fun getXdelta(): Int {
        return 0
    }

    override fun getXdelta(aim: Int): Int {
        return 0
    }

    override fun getYdelta(): Int {
        return -value
    }

    override fun getYdelta(aim: Int): Int {
        return 0
    }

    override fun getAimDelta(aim: Int): Int {
        return -value
    }
}
class DownCommand(value:Int): Command(value) {
    override fun getXdelta(): Int {
        return 0
    }

    override fun getXdelta(aim: Int): Int {
        return 0
    }

    override fun getYdelta(): Int {
        return value
    }

    override fun getYdelta(aim: Int): Int {
        return 0
    }

    override fun getAimDelta(aim: Int): Int {
        return value
    }
}
