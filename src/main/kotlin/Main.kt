import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun main(args: Array<String>) {
    employeesWorkedSameProject().map {
        getMostWorkedPair(it.value)
    }
}

fun getEmployeesData(): MutableList<Employee> {
    val employeesData = File(object {}.javaClass.getResource("data.txt")!!.toURI()).inputStream()
    val employeesList = mutableListOf<Employee>()
    employeesData.bufferedReader().forEachLine {
        if (it.isNotEmpty()) {
            val employeeData = it.split(",").toList()
            employeesList.add(
                createEmployee(employeeData)
            )
        }
    }
    return employeesList
}

private fun createEmployee(employeeData: List<String>) = Employee(
    employeeData.first(),
    employeeData[1].trim().toInt(),
    employeeData[2].trim(),
    if (employeeData[3].trim() != "NULL") employeeData[3].trim() else null,
    workedDays(
        calculateDateDiff(
            employeeData[2].trim(),
            if (employeeData[3].trim() != "NULL") employeeData[3].trim() else null
        )
    ),
)

fun employeesWorkedSameProject(): Map<Int, List<Employee>> {
    return getEmployeesData().groupBy { it.projectId }
}

fun getDateInMillis(date: String): Long {
    return SimpleDateFormat("yyyy-MM-dd").parse(date).time
}

fun calculateDateDiff(startDate: String, endDate: String?): Long {
    return if (endDate != null) {
        getDateInMillis(endDate) - getDateInMillis(startDate)
    } else Calendar.getInstance().timeInMillis - getDateInMillis(startDate)
}

fun workedDays(millis: Long): Long {
    return millis / 1000 / 60 / 60 / 24
}

fun getMostWorkedPair(employees: List<Employee>) {
    val sortedEmployeesList = employees.sortedByDescending { it.workedTimeInProject }
    if (sortedEmployeesList.size >= 2) {
        println("In project ${sortedEmployeesList[0].projectId} the most worked pair were the employees with these ids ${sortedEmployeesList[0].employeeId} and ${sortedEmployeesList[1].employeeId}")
    } else {
        println("There is only one employee In project ${sortedEmployeesList[0].projectId}")
    }
}
