package org.example.mathapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField inputN;  // Количество строк
    @FXML
    private TextField inputM;  // Количество столбцов
    @FXML
    private TextArea matrixInputArea;  // Поле для ввода коэффициентов
    @FXML
    private TextArea outputArea;  // Поле для вывода результатов

    // Обработчик для создания матрицы и вывода формулы
    @FXML
    public void onCreateMatrixClick() {
        try {
            String inputNText = inputN.getText();
            String inputMText = inputM.getText();

            // Проверка на пустые строки
            if (inputNText.isEmpty() || inputMText.isEmpty()) {
                outputArea.setText("Ошибка: Введите значения для количества строк и столбцов.");
                return;
            }

            int n = Integer.parseInt(inputNText);
            int m = Integer.parseInt(inputMText);

            // Проверка на корректность размера матрицы
            if (m < 2 || n < 1) {
                outputArea.setText("Ошибка: количество столбцов должно быть не меньше 2.");
                return;
            }

            // Генерация формул для системы уравнений
            StringBuilder equations = new StringBuilder("Формула:\n");
            for (int i = 0; i < n; i++) {
                StringBuilder equation = new StringBuilder();
                for (int j = 0; j < m - 1; j++) {
                    equation.append("a").append(i + 1).append(j + 1).append("x").append(j + 1);
                    if (j < m - 2) equation.append(" + ");
                }
                equation.append(" = b").append(i + 1).append("\n");
                equations.append(equation);
            }

            outputArea.setText(equations.toString());

        } catch (NumberFormatException e) {
            outputArea.setText("Ошибка: Введите корректные числовые значения.");
        }
    }

    // Обработчик для решения системы методом Гаусса
    @FXML
    protected void onSolveButtonClick() {
        int n = Integer.parseInt(inputN.getText());  // Количество строк
        int m = Integer.parseInt(inputM.getText());  // Количество столбцов

        if (m < 1 || n < 1 || m < 2) {
            outputArea.setText("Ошибка: количество столбцов должно быть не меньше 2.");
            return;
        }

        String[] lines = matrixInputArea.getText().split("\n");
        if (lines.length != n) {
            outputArea.setText("Ошибка: введите правильное количество строк.\n");
            return;
        }

        double[][] coefficients = new double[n][m];
        try {
            for (int i = 0; i < n; i++) {
                String[] values = lines[i].trim().split("\\s+");
                if (values.length != m) {
                    outputArea.setText("Ошибка: неправильное количество значений в строке " + (i + 1));
                    return;
                }

                for (int j = 0; j < m; j++) {
                    coefficients[i][j] = Double.parseDouble(values[j]);
                }
            }
        } catch (NumberFormatException e) {
            outputArea.setText("Ошибка: неверный формат чисел.");
            return;
        }

        outputArea.appendText("\nВведенная матрица:\n");
        printMatrix(coefficients, n, m);

        // Пошаговое решение методом Гаусса
        double[] solution = gaussElimination(coefficients);
        if (solution != null) {
            outputArea.appendText("\nРешение системы:\n");
            for (int i = 0; i < solution.length; i++) {
                outputArea.appendText("x" + (i + 1) + " = " + (int) solution[i] + "\n");
            }
        }
    }

    // Метод для вывода матрицы
    private void printMatrix(double[][] matrix, int n, int m) {
        for (int i = 0; i < n; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < m; j++) {
                row.append(String.format("%10.2f", matrix[i][j])).append(" ");
            }
            outputArea.appendText(row.toString() + "\n");
        }
    }

    // Метод для решения системы уравнений методом Гаусса с пошаговым выводом
    private double[] gaussElimination(double[][] A) {
        int n = A.length;
        int m = A[0].length;

        // Прямой ход метода Гаусса
        for (int i = 0; i < n; i++) {
            // Находим главный элемент
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[k][i]) > Math.abs(A[maxRow][i])) {
                    maxRow = k;
                }
            }

            // Меняем строки
            double[] temp = A[i];
            A[i] = A[maxRow];
            A[maxRow] = temp;

            // Проверка на деление на ноль
            if (Math.abs(A[i][i]) < 1e-10) {
                outputArea.appendText("Ошибка: деление на ноль.\n");
                return null;
            }

            // Приведение к треугольному виду
            for (int k = i + 1; k < n; k++) {
                double factor = A[k][i] / A[i][i];
                for (int j = i; j < m; j++) {
                    A[k][j] -= factor * A[i][j];
                }
            }

            // Вывод промежуточной матрицы
            outputArea.appendText("\nПосле преобразования строки " + (i + 1) + ":\n");
            printMatrix(A, n, m);
        }

        // Обратный ход
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = A[i][m - 1] / A[i][i];
            for (int j = i - 1; j >= 0; j--) {
                A[j][m - 1] -= A[j][i] * x[i];
            }
        }

        return x;
    }
}
