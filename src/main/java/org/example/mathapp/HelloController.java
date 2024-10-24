package org.example.mathapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class HelloController {

    @FXML
    private TextField inputN;  // Количество строк
    @FXML
    private TextField inputM;  // Количество столбцов
    @FXML
    private TextArea matrixInputArea;  // Поле для ввода коэффициентов
    @FXML
    private TextArea outputArea;  // Поле для вывода результатов






    @FXML
    private TextArea inverseMatrixInputArea; // Поле для ввода матрицы

    @FXML
    private TextArea inverseOutputArea; // Поле для вывода шагов решения обратной матрицы

    @FXML
    private TextField inverseInputN; // Поле для ввода размера матрицы

    @FXML
    private TextArea calculationOutputArea; // Поле для вывода результатов вычислений

    @FXML
    protected void onFindInverseMatrixClick() {
        int n = Integer.parseInt(inverseInputN.getText());  // Количество строк

        if (n <= 0) {
            inverseOutputArea.setText("Ошибка: размер матрицы должен быть положительным.");
            return;
        }

        String[] lines = inverseMatrixInputArea.getText().split("\n");
        if (lines.length != n) {
            inverseOutputArea.setText("Ошибка: введите правильное количество строк.\n");
            return;
        }

        double[][] coefficients = new double[n][n];
        try {
            for (int i = 0; i < n; i++) {
                String[] values = lines[i].trim().split("\\s+");
                if (values.length != n) {
                    inverseOutputArea.setText("Ошибка: неправильное количество значений в строке " + (i + 1));
                    return;
                }

                for (int j = 0; j < n; j++) {
                    coefficients[i][j] = Double.parseDouble(values[j]);
                }
            }
        } catch (NumberFormatException e) {
            inverseOutputArea.setText("Ошибка: неверный формат чисел.");
            return;
        }

        try {
            double[][] inverse = calculateInverse(coefficients);
            inverseOutputArea.appendText("Обратная матрица:\n");
            printMatrix(inverse, n, n, inverseOutputArea);
        } catch (Exception e) {
            inverseOutputArea.setText("Ошибка: матрица не является обратимой.");
        }
    }

    private double[][] transposeMatrix(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transposed = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }


    private double[][] calculateInverse(double[][] matrix) throws Exception {
        int n = matrix.length;
        double[][] identity = new double[n][n];
        for (int i = 0; i < n; i++) {
            identity[i][i] = 1;
        }

        double[][] aug = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, aug[i], 0, n);
            System.arraycopy(identity[i], 0, aug[i], n, n);
        }

        for (int i = 0; i < n; i++) {
            // Находим главный элемент
            double pivot = aug[i][i];
            if (Math.abs(pivot) < 1e-10) {
                throw new Exception("Матрица не является обратимой.");
            }

            for (int j = 0; j < 2 * n; j++) {
                aug[i][j] /= pivot;
            }

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    double factor = aug[j][i];
                    for (int k = 0; k < 2 * n; k++) {
                        aug[j][k] -= factor * aug[i][k];
                    }
                }
            }

            // Печать промежуточной матрицы в inverseOutputArea
            inverseOutputArea.appendText("Шаг " + (i + 1) + ":\n");
            printMatrix(aug, n, 2 * n, inverseOutputArea);
        }

        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(aug[i], n, inverse[i], 0, n);
        }
        return inverse;
    }

    private void printMatrix(double[][] matrix, int n, int m, TextArea outputArea) {
        for (int i = 0; i < n; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < m; j++) {
                row.append(String.format("%10.2f", matrix[i][j])).append(" ");
            }
            outputArea.appendText(row.toString() + "\n");
        }
    }

    @FXML
    protected void onTransposeMatrixClick() {
        String[] lines = inverseMatrixInputArea.getText().split("\n");
        double[][] coefficients;

        try {
            coefficients = new double[lines.length][];
            for (int i = 0; i < lines.length; i++) {
                String[] values = lines[i].trim().split("\\s+");
                coefficients[i] = new double[values.length];
                for (int j = 0; j < values.length; j++) {
                    coefficients[i][j] = Double.parseDouble(values[j]);
                }
            }

            double[][] transposed = transposeMatrix(coefficients);
            calculationOutputArea.appendText("Транспонированная матрица:\n");
            printMatrix(transposed, transposed.length, transposed[0].length, calculationOutputArea);

        } catch (NumberFormatException e) {
            calculationOutputArea.setText("Ошибка: неверный формат чисел.");
        }
    }





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
                continue;
            }

            // Приведение к треугольному виду
            for (int k = i + 1; k < n; k++) {
                double factor = A[k][i] / A[i][i];
                for (int j = i; j < m; j++) {
                    A[k][j] -= factor * A[i][j];
                }
            }
        }

        boolean infiniteSolutions = false;
        for (int i = 0; i < n; i++) {
            boolean isZeroRow = true;
            for (int j = 0; j < m - 1; j++) {
                if (Math.abs(A[i][j]) > 1e-10) {
                    isZeroRow = false;
                    break;
                }
            }
            if (isZeroRow) {
                if (Math.abs(A[i][m - 1]) > 1e-10) {
                    System.out.println("Система не имеет решений.");
                    return null;
                } else {
                    infiniteSolutions = true;
                }
            }
        }

        double[] x = new double[n];
        boolean[] isFreeVariable = new boolean[n];

        for (int i = n - 1; i >= 0; i--) {
            if (Math.abs(A[i][i]) > 1e-10) {
                x[i] = A[i][m - 1] / A[i][i];
                for (int j = i - 1; j >= 0; j--) {
                    A[j][m - 1] -= A[j][i] * x[i];
                }
            } else {
                x[i] = Double.NaN;
                isFreeVariable[i] = true;
            }
        }

        if (infiniteSolutions) {
            System.out.println("Система имеет бесконечное количество решений.");

            int freeVarIndex = -1;
            for (int i = 0; i < n; i++) {
                if (isFreeVariable[i]) {
                    freeVarIndex = i;
                    break;
                }
            }

            if (freeVarIndex != -1) {
                final int finalFreeVarIndex = freeVarIndex; // Сделаем индекс effectively final
                final double[] finalX = x; // Используем массив для передачи значений

                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Свободная переменная");
                dialog.setHeaderText("Введите значение для x" + (finalFreeVarIndex + 1));
                dialog.setContentText("Значение x" + (finalFreeVarIndex + 1) + ":");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(input -> {
                    try {
                        double value = Double.parseDouble(input);
                        finalX[finalFreeVarIndex] = value;

                        // Пересчитываем остальные переменные
                        for (int i = finalFreeVarIndex - 1; i >= 0; i--) {
                            if (!isFreeVariable[i]) {
                                finalX[i] = A[i][m - 1];
                                for (int j = i + 1; j < n; j++) {
                                    finalX[i] -= A[i][j] * finalX[j];
                                }
                                finalX[i] /= A[i][i];
                            }
                        }

                        // Вывод результата
                        System.out.println("Решение при x" + (finalFreeVarIndex + 1) + " = " + value + ":");
                        for (int i = 0; i < finalX.length; i++) {
                            System.out.println("x" + (i + 1) + " = " + finalX[i]);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: введите корректное число.");
                    }
                });
            }
        } else {
            System.out.println("Система имеет одно решение:");
            for (int i = 0; i < x.length; i++) {
                System.out.println("x" + (i + 1) + " = " + x[i]);
            }
        }

        return x;
    }


}
