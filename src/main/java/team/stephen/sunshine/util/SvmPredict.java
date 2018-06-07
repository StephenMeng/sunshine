package team.stephen.sunshine.util;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_print_interface;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by stephen on 2018/3/12.
 */
public class SvmPredict {
    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
    private static svm_print_interface svm_print_null = new svm_print_interface() {
        public void print(String s) {
        }
    };
    private static svm_print_interface svm_print_stdout = new svm_print_interface() {
        public void print(String s) {
            System.out.print(s);
        }
    };
    private static svm_print_interface svm_print_string;

    public SvmPredict() {
    }

    static void info(String s) {
        svm_print_string.print(s);
    }

    private static double atof(String s) {
        return Double.valueOf(s).doubleValue();
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    private static void predict(BufferedReader input, DataOutputStream output, svm_model model, int predict_probability) throws IOException {
        int correct = 0;
        int total = 0;
        double error = 0.0D;
        double sumv = 0.0D;
        double sumy = 0.0D;
        double sumvv = 0.0D;
        double sumyy = 0.0D;
        double sumvy = 0.0D;
        int svm_type = svm.svm_get_svm_type(model);
        int nr_class = svm.svm_get_nr_class(model);
        double[] prob_estimates = null;
        if (predict_probability == 1) {
            if (svm_type != 3 && svm_type != 4) {
                int[] labels = new int[nr_class];
                svm.svm_get_labels(model, labels);
                prob_estimates = new double[nr_class];
                output.writeBytes("labels");

                for (int j = 0; j < nr_class; ++j) {
                    output.writeBytes(" " + labels[j]);
                }

                output.writeBytes("\n");
            } else {
                info("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma=" + svm.svm_get_svr_probability(model) + "\n");
            }
        }

        while (true) {
            String line = input.readLine();
            if (line == null) {
                if (svm_type != 3 && svm_type != 4) {
                    info("Accuracy = " + (double) correct / (double) total * 100.0D + "% (" + correct + "/" + total + ") (classification)\n");
                } else {
                    info("Mean squared error = " + error / (double) total + " (regression)\n");
                    info("Squared correlation coefficient = " + ((double) total * sumvy - sumv * sumy) * ((double) total * sumvy - sumv * sumy) / (((double) total * sumvv - sumv * sumv) * ((double) total * sumyy - sumy * sumy)) + " (regression)\n");
                }

                return;
            }

            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            double target = atof(st.nextToken());
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];

            for (int j = 0; j < m; ++j) {
                x[j] = new svm_node();
                x[j].index = atoi(st.nextToken());
                x[j].value = atof(st.nextToken());
            }

            double v;
            if (predict_probability == 1 && (svm_type == 0 || svm_type == 1)) {
                v = svm.svm_predict_probability(model, x, prob_estimates);
                output.writeBytes(v + " ");

                for (int j = 0; j < nr_class; ++j) {
                    output.writeBytes(prob_estimates[j] + " ");
                }

                output.writeBytes("\n");
            } else {
                v = svm.svm_predict(model, x);
                output.writeBytes(v + "\n");
            }

            if (v == target) {
                ++correct;
            }

            error += (v - target) * (v - target);
            sumv += v;
            sumy += target;
            sumvv += v * v;
            sumyy += target * target;
            sumvy += v * target;
            ++total;
        }
    }

    private static void exit_with_help() {
        System.err.print("usage: SvmPredict [options] test_file model_file output_file\noptions:\n-b probability_estimates: whether to predict probability estimates, 0 or 1 (default 0); one-class SVM_KNN not supported yet\n-q : quiet mode (no outputs)\n");
        System.exit(1);
    }

    public static void main(String[] argv) throws IOException {
        int predict_probability = 1;
        svm_print_string = svm_print_stdout;

        int i;
        for (i = 0; i < argv.length && argv[i].charAt(0) == 45; ++i) {
            ++i;
            switch (argv[i - 1].charAt(1)) {
                case 'b':
                    predict_probability = atoi(argv[i]);
                    break;
                case 'q':
                    svm_print_string = svm_print_null;
                    --i;
                    break;
                default:
                    System.err.print("Unknown option: " + argv[i - 1] + "\n");
                    exit_with_help();
            }
        }

        if (i >= argv.length - 2) {
            exit_with_help();
        }

        try {
            BufferedReader input = new BufferedReader(new FileReader(argv[i]));
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(argv[i + 2])));
            svm_model model = svm.svm_load_model(argv[i + 1]);
            if (predict_probability == 1) {
                if (svm.svm_check_probability_model(model) == 0) {
                    System.err.print("Model does not support probabiliy estimates\n");
                    System.exit(1);
                }
            } else if (svm.svm_check_probability_model(model) != 0) {
                info("Model supports probability estimates, but disabled in prediction.\n");
            }

            predict(input, output, model, predict_probability);
            input.close();
            output.close();
        } catch (FileNotFoundException var6) {
            exit_with_help();
        } catch (ArrayIndexOutOfBoundsException var7) {
            exit_with_help();
        }

    }

    static {
        svm_print_string = svm_print_stdout;
    }
}
