fun main() {
    print("Ingrese la medida del lado del cuadrado:")
    val lado = readLine()!!.toInt()
    val perimetro = lado * 4
    println("El perímetro del cuadrado es $perimetro")
}