package advent

class Second: Base() {
    var commands = listOf<Command>()

    override fun parseAndStore(lines: List<String>) {
        commands = lines.map {
            val args = it.split(' ')
            Command.from(args[0], args[1].toInt())
        }
    }

    fun computeSimple():Int {
        val context = Context()
        commands.forEach { it.execute(context) }
        return context.compute()
    }

    fun computeWithAim(): Int {
        val context = Context(computeWithAim = true)
        commands.forEach { it.execute(context) }
        return context.compute()
    }
}

data class Context(var x: Int = 0, var y: Int = 0, var aim: Int = 0, val computeWithAim: Boolean = false) {
    fun compute(): Int {
        return x * y
    }
}

sealed class Command(val value: Int) {
    abstract fun execute(context: Context)

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
    override fun execute(context: Context) {
        context.x += value
        if (context.computeWithAim) {
            context.y += value * context.aim
        }
    }
}
class UpCommand(value:Int): Command(value) {
    override fun execute(context: Context) {
        if (context.computeWithAim) {
            context.aim -= value
        } else {
            context.y -= value
        }
    }
}
class DownCommand(value:Int): Command(value) {
    override fun execute(context: Context) {
        if (context.computeWithAim) {
            context.aim += value
        } else {
            context.y += value
        }
    }
}
