package com.example.lol.benchmark

import android.os.SystemClock
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import java.io.File
import java.util.Locale
import kotlin.math.ceil
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateProjectFlowBenchmark {

    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val appPackage = "com.example.lol"
    private val benchmarkActivityComponent = "com.example.lol/.benchmark.CreateProjectBenchmarkActivity"
    private val benchmarkTag = "CreateProjectFlowBenchmark"

    @Test
    fun benchmarkCreateProjectFlow() {
        val samples = mutableListOf<BenchmarkSample>()

        repeat(8) { iterationIndex ->
            val iteration = iterationIndex + 1
            device.executeShellCommand("am force-stop $appPackage")

            val loadScreenMs = launchCreateProjectBenchmarkActivity()
            waitObject(By.desc("benchmark_project_name_input"), 10_000)

            val imagePickerProxyMs = measureImagePickerProxy()
            val formValidationMs = measureFormValidation(iteration)
            val submitUiMs = measureSubmitUiLatency()
            val memoryPssKb = captureMemoryPssKb()
            val cpuPercent = captureCpuPercent()

            samples +=
                    BenchmarkSample(
                            iteration = iteration,
                            loadScreenMs = loadScreenMs,
                            imagePickerProxyMs = imagePickerProxyMs,
                            formValidationMs = formValidationMs,
                            submitUiMs = submitUiMs,
                            memoryPssKb = memoryPssKb,
                            cpuPercent = cpuPercent
                    )
        }

        val outputDir = File(InstrumentationRegistry.getInstrumentation().context.filesDir, "benchmarks")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val rawFile = File(outputDir, "create_project_ui_raw.csv")
        rawFile.writeText(
                buildString {
                    appendLine(
                            "iteration,load_screen_ms,image_picker_proxy_ms,form_validation_ms,submit_ui_ms,memory_pss_kb,cpu_percent"
                    )
                    for (sample in samples) {
                        appendLine(
                                "${sample.iteration},${sample.loadScreenMs.format(2)},${sample.imagePickerProxyMs.format(2)},${sample.formValidationMs.format(2)},${sample.submitUiMs.format(2)},${sample.memoryPssKb},${sample.cpuPercent.format(2)}"
                        )
                    }
                }
        )

        val summaryFile = File(outputDir, "create_project_ui_summary.csv")
        summaryFile.writeText(
                buildString {
                    appendLine("metric,min_ms,avg_ms,p95_ms,max_ms")
                    appendMetricSummaryLine("load_screen", samples.map { it.loadScreenMs })
                    appendMetricSummaryLine("image_picker_proxy", samples.map { it.imagePickerProxyMs })
                    appendMetricSummaryLine("form_validation", samples.map { it.formValidationMs })
                    appendMetricSummaryLine("submit_ui", samples.map { it.submitUiMs })
                }
        )

        val profilingFile = File(outputDir, "create_project_profile_snapshot.csv")
        profilingFile.writeText(
                buildString {
                    appendLine("metric,min,avg,max")
                    appendLine(
                            "memory_pss_kb,${samples.minOf { it.memoryPssKb }},${samples.map { it.memoryPssKb }.average().format(0)},${samples.maxOf { it.memoryPssKb }}"
                    )
                    appendLine(
                            "cpu_percent,${samples.minOf { it.cpuPercent }.format(2)},${samples.map { it.cpuPercent }.average().format(2)},${samples.maxOf { it.cpuPercent }.format(2)}"
                    )
                }
        )

        Log.i(benchmarkTag, "UI_BENCHMARK_RAW=${rawFile.absolutePath}")
        Log.i(benchmarkTag, "UI_BENCHMARK_SUMMARY=${summaryFile.absolutePath}")
        Log.i(benchmarkTag, "UI_BENCHMARK_PROFILE=${profilingFile.absolutePath}")
        println("UI_BENCHMARK_RAW=${rawFile.absolutePath}")
        println("UI_BENCHMARK_SUMMARY=${summaryFile.absolutePath}")
        println("UI_BENCHMARK_PROFILE=${profilingFile.absolutePath}")

        rawFile.readLines().drop(1).forEach { line ->
            Log.i(benchmarkTag, "BENCH_UI_RAW,$line")
            println("BENCH_UI_RAW,$line")
        }
        summaryFile.readLines().drop(1).forEach { line ->
            Log.i(benchmarkTag, "BENCH_UI_SUMMARY,$line")
            println("BENCH_UI_SUMMARY,$line")
        }
        profilingFile.readLines().drop(1).forEach { line ->
            Log.i(benchmarkTag, "BENCH_UI_PROFILE,$line")
            println("BENCH_UI_PROFILE,$line")
        }
    }

    private fun launchCreateProjectBenchmarkActivity(): Double {
        val commandOutput = device.executeShellCommand("am start -W -n $benchmarkActivityComponent")
        val totalTimeMs =
                Regex("TotalTime:\\s*(\\d+)")
                        .find(commandOutput)
                        ?.groupValues
                        ?.getOrNull(1)
                        ?.toDoubleOrNull()
                        ?: error("Cannot parse TotalTime from command output: $commandOutput")

        waitObject(By.text("Создать проект"), 10_000)
        return totalTimeMs
    }

    private fun measureImagePickerProxy(): Double {
        val photoBox = waitObject(By.desc("benchmark_photo_picker_area"), 5_000)
        val started = SystemClock.elapsedRealtimeNanos()
        photoBox.click()
        waitObject(By.text("Выбрать из галереи"), 5_000)
        device.pressBack()
        waitObject(By.text("Создать проект"), 5_000)
        return nanosToMs(SystemClock.elapsedRealtimeNanos() - started)
    }

    private fun measureFormValidation(iteration: Int): Double {
        val projectNameField = waitProjectNameEditText(5_000)
        projectNameField.setText("")
        SystemClock.sleep(100)

        val started = SystemClock.elapsedRealtimeNanos()
        projectNameField.setText("Benchmark project $iteration")
        SystemClock.sleep(120)
        return nanosToMs(SystemClock.elapsedRealtimeNanos() - started)
    }

    private fun measureSubmitUiLatency(): Double {
        val submitButton = waitSubmitButton(8_000)
        if (!submitButton.isEnabled) {
            error("Submit button is visible but disabled")
        }
        val started = SystemClock.elapsedRealtimeNanos()
        submitButton.click()
        waitObject(By.desc("benchmark_submit_complete"), 8_000)
        return nanosToMs(SystemClock.elapsedRealtimeNanos() - started)
    }

    private fun waitProjectNameEditText(timeoutMs: Long): UiObject2 {
        val container = waitObject(By.desc("benchmark_project_name_input"), timeoutMs)
        return container.findObject(By.clazz("android.widget.EditText"))
                ?: error("Project name EditText not found in benchmark container")
    }

    private fun waitSubmitButton(timeoutMs: Long): UiObject2 {
        val deadline = SystemClock.elapsedRealtime() + timeoutMs
        while (SystemClock.elapsedRealtime() < deadline) {
            val submitButton = device.findObject(By.desc("benchmark_submit_button"))
            if (submitButton != null) {
                return submitButton
            }
            device.swipe(640, 2500, 640, 1200, 20)
            SystemClock.sleep(250)
        }
        error("Submit button not found after scrolling")
    }

    private fun captureMemoryPssKb(): Int {
        val output = device.executeShellCommand("dumpsys meminfo $appPackage")
        val byAppSummary = Regex("TOTAL\\s+(\\d+)").find(output)?.groupValues?.getOrNull(1)
        val byPssLabel = Regex("TOTAL PSS:\\s*(\\d+)").find(output)?.groupValues?.getOrNull(1)
        return (byPssLabel ?: byAppSummary)?.toIntOrNull() ?: -1
    }

    private fun captureCpuPercent(): Double {
        val output = device.executeShellCommand("dumpsys cpuinfo | grep $appPackage")
        return Regex("([0-9]+(?:\\.[0-9]+)?)%")
                .find(output)
                ?.groupValues
                ?.getOrNull(1)
                ?.toDoubleOrNull()
                ?: -1.0
    }

    private fun waitObject(selector: BySelector, timeoutMs: Long): UiObject2 {
        return device.wait(Until.findObject(selector), timeoutMs)
                ?: error("Object not found for selector=$selector in ${timeoutMs}ms")
    }

    private fun nanosToMs(value: Long): Double = value / 1_000_000.0

    private fun Double.format(decimals: Int): String {
        return String.format(Locale.US, "%.${decimals}f", this)
    }

    private fun StringBuilder.appendMetricSummaryLine(metricName: String, values: List<Double>) {
        val sorted = values.sorted()
        val min = sorted.firstOrNull() ?: 0.0
        val avg = values.average()
        val p95 = percentile(sorted, 95)
        val max = sorted.lastOrNull() ?: 0.0
        appendLine(
                "$metricName,${min.format(2)},${avg.format(2)},${p95.format(2)},${max.format(2)}"
        )
    }

    private fun percentile(sortedValues: List<Double>, percentile: Int): Double {
        if (sortedValues.isEmpty()) return 0.0
        val index = ceil((percentile / 100.0) * sortedValues.size).toInt().coerceIn(1, sortedValues.size)
        return sortedValues[index - 1]
    }

    private data class BenchmarkSample(
            val iteration: Int,
            val loadScreenMs: Double,
            val imagePickerProxyMs: Double,
            val formValidationMs: Double,
            val submitUiMs: Double,
            val memoryPssKb: Int,
            val cpuPercent: Double
    )
}
