package org.nogard.mutual_information;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    private EditText[][] editTextArray = new EditText[4][4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Theoretical Computer Science");

        editTextArray[0][0] = findViewById(R.id.input_1_1);
        editTextArray[0][1] = findViewById(R.id.input_1_2);
        editTextArray[0][2] = findViewById(R.id.input_1_3);
        editTextArray[0][3] = findViewById(R.id.input_1_4);

        editTextArray[1][0] = findViewById(R.id.input_2_1);
        editTextArray[1][1] = findViewById(R.id.input_2_2);
        editTextArray[1][2] = findViewById(R.id.input_2_3);
        editTextArray[1][3] = findViewById(R.id.input_2_4);

        editTextArray[2][0] = findViewById(R.id.input_3_1);
        editTextArray[2][1] = findViewById(R.id.input_3_2);
        editTextArray[2][2] = findViewById(R.id.input_3_3);
        editTextArray[2][3] = findViewById(R.id.input_3_4);

        editTextArray[3][0] = findViewById(R.id.input_4_1);
        editTextArray[3][1] = findViewById(R.id.input_4_2);
        editTextArray[3][2] = findViewById(R.id.input_4_3);
        editTextArray[3][3] = findViewById(R.id.input_4_4);

        Button calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processInputValues();
            }
        });
    }

    private void processInputValues() {
        double[][] values = new double[4][4];
        double[] rowSums = new double[4];
        double[] columnSums = new double[4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String inputValue = editTextArray[i][j].getText().toString();
                if (!inputValue.isEmpty()) {
                    try {
                        Expression expression = new ExpressionBuilder(inputValue).build();

                        values[i][j] = expression.evaluate();

                        rowSums[i] += values[i][j];
                        columnSums[j] += values[i][j];
                    } catch (Exception e) {
                        Log.e("MyTag", "Invalid expression at [" + i + "][" + j + "]");
                    }
                }
            }
        }

        double mutualEntropy = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double probability = values[i][j];
                if (probability > 0) {
                    mutualEntropy -= probability * (Math.log(probability) / Math.log(2));
                }
            }
        }

        double mutualInformation = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double jointProbability = values[i][j];
                double probabilityA = rowSums[i];
                double probabilityB = columnSums[j];

                if (jointProbability > 0 && probabilityA > 0 && probabilityB > 0) {
                    mutualInformation += jointProbability * (Math.log(jointProbability / (probabilityA * probabilityB)) / Math.log(2));
                }
            }
        }

        TextView mutualEntropyTextView = findViewById(R.id.mutual_entropy_result);
        mutualEntropyTextView.setText("= " + mutualEntropy);

        TextView mutualInformationTextView = findViewById(R.id.mutual_information_result);
        mutualInformationTextView.setText("= " + mutualInformation);

//
//// Inicializace matice P(Y|X)
//        double[][] p_y_given_x = new double[4][4];
//
//// Výpočet podmíněných pravděpodobností
//        for (int x = 0; x < 4; x++) {
//            double sum_prob_y_given_x = 0;
//            for (int y = 0; y < 4; y++) {
//                sum_prob_y_given_x += values[x][y];  // Suma pravděpodobností P(y|x)
//            }
//            for (int y = 0; y < 4; y++) {
//                p_y_given_x[y][x] = values[x][y] / sum_prob_y_given_x;
//            }
//        }
//
//        double[][] p_y_given_x2 = new double[4][4];
//
//        for (int x = 0; x < 4; x++) {
//            for (int y = 0; y < 4; y++) {
//                p_y_given_x2[y][x] = p_y_given_x[y][x];
//            }
//        }
//
//        for (int x = 0; x < 4; x++) {
//            for (int y = 0; y < 4; y++) {
//                p_y_given_x[x][y] = p_y_given_x2[y][x];
//            }
//        }
//
//        double lowerBoundCapacity = 0;
//        int m = p_y_given_x.length;
//        int n = p_y_given_x[0].length;
//        double thresh = 1e-12;
//        int max_iter = 1000;
//        int log_base = 2;
//
//        Log.i("MyTag", "p_y_given_x:");
//        Log.i("MyTag1", "Value at [0][0]: " + values[0][0]);
//
//        for (int i = 0; i < m; i++) {
//            StringBuilder rowStringBuilder = new StringBuilder();
//            for (int j = 0; j < n; j++) {
//                rowStringBuilder.append(p_y_given_x[i][j]).append(" ");
//            }
//            Log.i("MyTag", rowStringBuilder.toString());
//        }
//
//        // works until now
//
//
//        // Input test
//        double sumRowMean = 0.0;
//        for (int i = 0; i < m; i++) {
//            double sumRow = 0.0;
//            for (int j = 0; j < n; j++) {
//                sumRow += p_y_given_x[i][j];
//            }
//            sumRowMean += sumRow / n;
//        }
//
//        double[][] r = new double[1][m];
//        for (int i = 0; i < m; i++) {
//            r[0][i] = 1.0 / m;
//        }
//
//        double[][] q = new double[m][n];
//        double[][] r1 = new double[m][1];
//
//        for (int iteration = 0; iteration < (int) max_iter; iteration++) {
//            // Calculate q matrix
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < m; j++) {
//                    q[i][j] = p_y_given_x[i][j]; // r[0][i] *
//                }
//            }
//
//            double[] qSum = new double[m];
//            for (int i = 0; i < m; i++) {
//                qSum[i]=0;
//                for (int j = 0; j < n; j++) {
//                    qSum[i] += q[j][i];
//                }
//            }
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < m; j++) {
//                    q[j][i] /= qSum[i];
//                }
//            }
//
//            for (int i = 0; i < m; i++) {
//                double prod = 1.0;
//                for (int j = 0; j < n; j++) {
//                    prod *= Math.pow(q[i][j], p_y_given_x[i][j]);
//                }
//                r1[i][0] = prod;
//            }
//
//
//
//            double r1Sum = 0.0;
//            for (int i = 0; i < m; i++) {
//                r1Sum += r1[i][0];
//            }
//            for (int i = 0; i < m; i++) {
//                r1[i][0] /= r1Sum;
//            }
//
//            double tolerance = 0.0;
//            for (int i = 0; i < m; i++) {
//                tolerance += Math.pow(r1[i][0] - r[0][i], 2);
//            }
//            tolerance = Math.sqrt(tolerance);
//
//            for (int i = 0; i < m; i++) {
//                r[0][i]=r1[i][0];
//            }
//
//            if (tolerance < thresh) {
//                break;
//            }
//        }
//
//
//
//        Log.i("Q", "q: ");
//        for (int i = 0; i < m; i++) {
//            StringBuilder rowStringBuilder = new StringBuilder();
//            for (int j = 0; j < n; j++) {
//                rowStringBuilder.append(q[i][j]).append(" ");
//            }
//            Log.i("Q", rowStringBuilder.toString());
//        }
//
//
//
//
//        Log.i("R1", "r1:");
//
//        for (int i = 0; i < m; i++) {
//            StringBuilder rowStringBuilder = new StringBuilder();
//            rowStringBuilder.append(r1[i][0]).append(" ");
//            Log.i("R1", rowStringBuilder.toString());
//        }
//
//
//        double[] rArr = r[0];
//
//        double c = 0.0;
//        for (int i = 0; i < m; i++) {
//            if (rArr[i] > 0) {
//                for (int j = 0; j < n; j++) {
//                    c += rArr[i] * p_y_given_x[i][j] * Math.log(q[i][j] / rArr[i] + 1e-16);
//                }
//            }
//        }
//        c /= Math.log(2); // naaah, not there
//
        TextView lowerBoundCapacityTextView = findViewById(R.id.lower_bound_capacity);
        lowerBoundCapacityTextView.setText("= " + mutualInformation);


    }



}