package kz.kaznu.aiym.first;

import java.util.Locale;

/*
 * Метод Ньютона (касательных) для уравнения f(x) = 0 на отрезке [a; b].
 * Пример: f(x) = x^2 - 2 на [1; 2], x0 = 1.5, eps = 1e-10 (корень = sqrt(2)).
 *
 * Формула: x_{n+1} = x_n - f(x_n) / f'(x_n)
 * Остановка: |x_{n+1} - x_n| < eps (дополнительно выводится невязка |f(x_{n+1})|).
 */
public class MainApp {

    /** Исходная функция f(x) = x^2 - 2. */
    static double f(double x) {
        return x * x - 2;
    }

    /** Производная f'(x) = 2x. */
    static double df(double x) {
        return 2 * x;
    }

    public static void main(String[] args) {
        double a = 1.0, b = 2.0;     // отрезок
        double x = 1.5;              // начальное приближение x0
        double eps = 1e-10;          // требуемая точность
        int maxIter = 50;            // защита от зацикливания/расходимости

        // 1. Проверка существования корня: f(a) и f(b) разных знаков
        if (f(a) * f(b) >= 0) {
            System.out.println("На [a; b] нет гарантии корня: f(a)*f(b) >= 0");
            return;
        }
        // 2. Проверка начального приближения (условие Фурье): f(x0)*f''(x0) > 0, здесь f'' = 2
        double d2 = 2;
        if (x < a || x > b || f(x) * d2 <= 0) {
            System.out.println("Предупреждение: x0 не удовлетворяет условию Фурье, "
                    + "сходимость не гарантирована теоремой.");
        }

        System.out.printf(Locale.US, "%2s %18s %14s %12s %18s %12s %12s%n",
                "n", "x_n", "f(x_n)", "f'(x_n)", "x_{n+1}", "|шаг|", "|f(x_{n+1})|");

        for (int n = 0; n < maxIter; n++) {
            double fx = f(x);
            double dfx = df(x);

            if (dfx == 0) {
                System.out.println("f'(x_n) = 0: касательная горизонтальна, метод остановлен.");
                return;
            }

            double xNext = x - fx / dfx;          // формула Ньютона
            double step = Math.abs(xNext - x);    // изменение приближения
            double residual = Math.abs(f(xNext)); // невязка

            System.out.printf(Locale.US, "%2d %18.12f %14.6e %12.6f %18.12f %12.3e %12.3e%n",
                    n, x, fx, dfx, xNext, step, residual);

            x = xNext;

            if (x < a || x > b) {
                System.out.println("Приближение вышло за пределы отрезка [a; b].");
                return;
            }
            if (step < eps) {                     // критерий остановки
                System.out.println();
                System.out.printf(Locale.US, "Ответ: x* ≈ %.12f%n", x);
                System.out.printf(Locale.US, "Итераций: %d%n", n + 1);
                System.out.printf(Locale.US, "Проверка: sqrt(2) = %.12f, f(x*) = %.3e%n",
                        Math.sqrt(2), f(x));
                return;
            }
        }
        System.out.println("Достигнуто максимальное число итераций без достижения точности.");
    }
}