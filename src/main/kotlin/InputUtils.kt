import java.util.*

class InputUtils {
    private val input = Scanner(System.`in`)

    fun readCommand(max: Int): Int {
        while (true) {
            val input = validateInput(input.nextLine())
            if (input <= -1 || input > max) {
                println("Число должно быть от 0 до $max. Повторите ввод:")
                continue
            }
            return input
        }
    }

    fun showAlbumMenu(tittleScreen: String, commandList: List<String>) {
        println("======")
        println("Группа $tittleScreen:\n")
        commandList.forEachIndexed { index, command ->
            println("$index. $command")
        }
        println("------")
        print("Выберите альбом для скачивания:")
    }

    fun endPrintScreen() {
        println("======\n")
    }

    private fun validateInput(input: String): Int {
        return input.toIntOrNull() ?: -1
    }

}