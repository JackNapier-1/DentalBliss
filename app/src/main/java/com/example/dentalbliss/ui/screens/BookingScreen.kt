package com.example.dentalbliss.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dentalbliss.data.AppointmentData
import com.example.dentalbliss.data.AppointmentRepository
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

private const val maxAppointmentsPerDay = 3 // Demo: only 3 slots per day

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var selectedService by remember { mutableStateOf<String?>(null) }
    var patientName by remember { mutableStateOf("") }
    var bookingSuccess by remember { mutableStateOf(false) }
    var bookingError by remember { mutableStateOf<String?>(null) }

    val services = listOf(
        "Dental Cleaning",
        "Tooth Extraction",
        "Root Canal",
        "Dental Filling",
        "Teeth Whitening"
    )

    val availableTimes = listOf(
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        LocalTime.of(11, 0),
        LocalTime.of(13, 0),
        LocalTime.of(14, 0),
        LocalTime.of(15, 0)
    )

    val fullyBookedDates = AppointmentRepository.appointments
        .groupBy { it.date }
        .filter { it.value.size >= maxAppointmentsPerDay }
        .keys

    val bookedTimesForDate = AppointmentRepository.appointments
        .filter { it.date == selectedDate }
        .map { it.time }

    if (showDatePicker) {
        val now = selectedDate
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val picked = LocalDate.of(year, month + 1, dayOfMonth)
                if (!fullyBookedDates.contains(picked) && !picked.isBefore(LocalDate.now())) {
                    selectedDate = picked
                }
                showDatePicker = false
            },
            now.year,
            now.monthValue - 1,
            now.dayOfMonth
        ).apply {
            datePicker.minDate = Calendar.getInstance().timeInMillis
        }.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Appointment") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Patient Name
            OutlinedTextField(
                value = patientName,
                onValueChange = { patientName = it },
                label = { Text("Your Name", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth()
            )
            // Date Picker
            Text(text = "Select Date", style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            Button(onClick = { showDatePicker = true }) {
                Text(selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")), fontSize = 16.sp)
            }

            // Service Selection
            Text(text = "Select Service", style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            services.forEach { service ->
                ServiceItem(
                    service = service,
                    isSelected = selectedService == service,
                    onSelect = { selectedService = service }
                )
            }

            // Time Selection
            Text(text = "Select Time", style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            availableTimes.forEach { time ->
                val isBooked = bookedTimesForDate.contains(time)
                TimeItem(
                    time = time,
                    isSelected = selectedTime == time,
                    isBooked = isBooked,
                    onSelect = { if (!isBooked) selectedTime = time }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Book/Send Button
            Button(
                onClick = {
                    if (selectedService != null && selectedTime != null && patientName.isNotBlank()) {
                        if (AppointmentRepository.appointments.count { it.date == selectedDate } < maxAppointmentsPerDay) {
                            if (!bookedTimesForDate.contains(selectedTime)) {
                                AppointmentRepository.appointments.add(
                                    AppointmentData(
                                        date = selectedDate,
                                        time = selectedTime!!,
                                        service = selectedService!!,
                                        patientName = patientName
                                    )
                                )
                                bookingSuccess = true
                                bookingError = null
                            } else {
                                bookingError = "This time slot is already booked."
                            }
                        } else {
                            bookingError = "This date is fully booked."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedService != null && selectedTime != null && patientName.isNotBlank()
            ) {
                Text(text = "Book Appointment", fontSize = 18.sp)
            }

            if (bookingSuccess) {
                Text(
                    text = "Appointment booked successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }
            if (bookingError != null) {
                Text(
                    text = bookingError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp
                )
            }

            // List of Booked Appointments (for this patient)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Your Appointments", style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            val myAppointments = AppointmentRepository.appointments.filter { it.patientName == patientName }
            if (myAppointments.isEmpty()) {
                Text("No appointments yet.", fontSize = 16.sp)
            } else {
                myAppointments.forEach {
                    Text("${it.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))} at ${it.time.format(DateTimeFormatter.ofPattern("hh:mm a"))} - ${it.service}", fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ServiceItem(
    service: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = service)
        }
    }
}

@Composable
fun TimeItem(
    time: LocalTime,
    isSelected: Boolean,
    isBooked: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isBooked -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = if (!isBooked) onSelect else null,
                enabled = !isBooked
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = time.format(DateTimeFormatter.ofPattern("hh:mm a")))
            if (isBooked) Text(" (Booked)", color = MaterialTheme.colorScheme.error)
        }
    }
} 