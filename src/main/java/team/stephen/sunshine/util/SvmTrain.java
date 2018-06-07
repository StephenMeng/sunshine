package team.stephen.sunshine.util;

import libsvm.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by stephen on 2018/3/12.
 */
public class SvmTrain {
    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
    private svm_parameter param;
    private svm_problem prob;
    private svm_model model;
    private String input_file_name;
    private String model_file_name;
    private String error_msg;
    private int cross_validation;
    private int nr_fold;
    private static svm_print_interface svm_print_null = new svm_print_interface() {
        public void print(String s) {
        }
    };

    public SvmTrain() {
    }

    private static void exit_with_help() {
        System.out.print("Usage: svm_train [options] training_set_file [model_file]\noptions:\n-s svm_type : set type of SVM_KNN (default 0)\n\t0 -- C-SVC\t\t(multi-class classification)\n\t1 -- nu-SVC\t\t(multi-class classification)\n\t2 -- one-class SVM_KNN\n\t3 -- epsilon-SVR\t(regression)\n\t4 -- nu-SVR\t\t(regression)\n-t kernel_type : set type of kernel function (default 2)\n\t0 -- linear: u'*v\n\t1 -- polynomial: (gamma*u'*v + coef0)^degree\n\t2 -- radial basis function: exp(-gamma*|u-v|^2)\n\t3 -- sigmoid: tanh(gamma*u'*v + coef0)\n\t4 -- precomputed kernel (kernel values in training_set_file)\n-d degree : set degree in kernel function (default 3)\n-g gamma : set gamma in kernel function (default 1/num_features)\n-r coef0 : set coef0 in kernel function (default 0)\n-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\n-n nu : set the parameter nu of nu-SVC, one-class SVM_KNN, and nu-SVR (default 0.5)\n-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\n-m cachesize : set cache memory size in MB (default 100)\n-e epsilon : set tolerance of termination criterion (default 0.001)\n-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)\n-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\n-wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)\n-v n : n-fold cross validation mode\n-q : quiet mode (no outputs)\n");
        System.exit(1);
    }

    private void do_cross_validation() {
        int total_correct = 0;
        double total_error = 0.0D;
        double sumv = 0.0D;
        double sumy = 0.0D;
        double sumvv = 0.0D;
        double sumyy = 0.0D;
        double sumvy = 0.0D;
        double[] target = new double[this.prob.l];
        svm.svm_cross_validation(this.prob, this.param, this.nr_fold, target);
        int i;
        if (this.param.svm_type != 3 && this.param.svm_type != 4) {
            for (i = 0; i < this.prob.l; ++i) {
                if (target[i] == this.prob.y[i]) {
                    ++total_correct;
                }
            }

            System.out.print("Cross Validation Accuracy = " + 100.0D * (double) total_correct / (double) this.prob.l + "%\n");
        } else {
            for (i = 0; i < this.prob.l; ++i) {
                double y = this.prob.y[i];
                double v = target[i];
                total_error += (v - y) * (v - y);
                sumv += v;
                sumy += y;
                sumvv += v * v;
                sumyy += y * y;
                sumvy += v * y;
            }

            System.out.print("Cross Validation Mean squared error = " + total_error / (double) this.prob.l + "\n");
            System.out.print("Cross Validation Squared correlation coefficient = " + ((double) this.prob.l * sumvy - sumv * sumy) * ((double) this.prob.l * sumvy - sumv * sumy) / (((double) this.prob.l * sumvv - sumv * sumv) * ((double) this.prob.l * sumyy - sumy * sumy)) + "\n");
        }

    }

    private void run(String[] argv) throws IOException {
        this.parse_command_line(argv);
        this.read_problem();
        this.error_msg = svm.svm_check_parameter(this.prob, this.param);
        if (this.error_msg != null) {
            System.err.print("ERROR: " + this.error_msg + "\n");
            System.exit(1);
        }

        if (this.cross_validation != 0) {
            this.do_cross_validation();
        } else {
            this.model = svm.svm_train(this.prob, this.param);
            svm.svm_save_model(this.model_file_name, this.model);
        }

    }

    public static void main(String[] argv) throws IOException {
        SvmTrain t = new SvmTrain();
        t.run(argv);
    }

    private static double atof(String s) {
        double d = Double.valueOf(s).doubleValue();
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            System.err.print("NaN or Infinity in input\n");
            System.exit(1);
        }

        return d;
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    private void parse_command_line(String[] argv) {
        svm_print_interface print_func = null;
        this.param = new svm_parameter();
        this.param.svm_type = 0;
        this.param.kernel_type = 2;
        this.param.degree = 3;
        this.param.gamma = 0.0D;
        this.param.coef0 = 0.0D;
        this.param.nu = 0.5D;
        this.param.cache_size = 100.0D;
        this.param.C = 1.0D;
        this.param.eps = 0.001D;
        this.param.p = 0.1D;
        this.param.shrinking = 1;
        this.param.probability = 0;
        this.param.nr_weight = 0;
        this.param.weight_label = new int[0];
        this.param.weight = new double[0];
        this.cross_validation = 0;

        int i;
        for (i = 0; i < argv.length && argv[i].charAt(0) == 45; ++i) {
            ++i;
            if (i >= argv.length) {
                exit_with_help();
            }

            switch (argv[i - 1].charAt(1)) {
                case 'b':
                    this.param.probability = atoi(argv[i]);
                    break;
                case 'c':
                    this.param.C = atof(argv[i]);
                    break;
                case 'd':
                    this.param.degree = atoi(argv[i]);
                    break;
                case 'e':
                    this.param.eps = atof(argv[i]);
                    break;
                case 'f':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'o':
                case 'u':
                default:
                    System.err.print("Unknown option: " + argv[i - 1] + "\n");
                    exit_with_help();
                    break;
                case 'g':
                    this.param.gamma = atof(argv[i]);
                    break;
                case 'h':
                    this.param.shrinking = atoi(argv[i]);
                    break;
                case 'm':
                    this.param.cache_size = atof(argv[i]);
                    break;
                case 'n':
                    this.param.nu = atof(argv[i]);
                    break;
                case 'p':
                    this.param.p = atof(argv[i]);
                    break;
                case 'q':
                    print_func = svm_print_null;
                    --i;
                    break;
                case 'r':
                    this.param.coef0 = atof(argv[i]);
                    break;
                case 's':
                    this.param.svm_type = atoi(argv[i]);
                    break;
                case 't':
                    this.param.kernel_type = atoi(argv[i]);
                    break;
                case 'v':
                    this.cross_validation = 1;
                    this.nr_fold = atoi(argv[i]);
                    if (this.nr_fold < 2) {
                        System.err.print("n-fold cross validation: n must >= 2\n");
                        exit_with_help();
                    }
                    break;
                case 'w':
                    ++this.param.nr_weight;
                    int[] old = this.param.weight_label;
                    this.param.weight_label = new int[this.param.nr_weight];
                    System.arraycopy(old, 0, this.param.weight_label, 0, this.param.nr_weight - 1);
                    double[] oldd = this.param.weight;
                    this.param.weight = new double[this.param.nr_weight];
                    System.arraycopy(old, 0, this.param.weight, 0, this.param.nr_weight - 1);
                    this.param.weight_label[this.param.nr_weight - 1] = atoi(argv[i - 1].substring(2));
                    this.param.weight[this.param.nr_weight - 1] = atof(argv[i]);
            }
        }

        svm.svm_set_print_string_function(print_func);
        if (i >= argv.length) {
            exit_with_help();
        }

        this.input_file_name = argv[i];
        if (i < argv.length - 1) {
            this.model_file_name = argv[i + 1];
        } else {
            int p = argv[i].lastIndexOf(47);
            ++p;
            this.model_file_name = argv[i].substring(p) + ".model";
        }

    }

    private void read_problem() throws IOException {
        BufferedReader fp = new BufferedReader(new FileReader(this.input_file_name));
        Vector<Double> vy = new Vector();
        Vector<svm_node[]> vx = new Vector();
        int max_index = 0;

        while (true) {
            String line = fp.readLine();
            if (line == null) {
                this.prob = new svm_problem();
                this.prob.l = vy.size();
                this.prob.x = new svm_node[this.prob.l][];

                int i;
                for (i = 0; i < this.prob.l; ++i) {
                    this.prob.x[i] = (svm_node[]) vx.elementAt(i);
                }

                this.prob.y = new double[this.prob.l];

                for (i = 0; i < this.prob.l; ++i) {
                    this.prob.y[i] = ((Double) vy.elementAt(i)).doubleValue();
                }

                if (this.param.gamma == 0.0D && max_index > 0) {
                    this.param.gamma = 1.0D / (double) max_index;
                }

                if (this.param.kernel_type == 4) {
                    for (i = 0; i < this.prob.l; ++i) {
                        if (this.prob.x[i][0].index != 0) {
                            System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                            System.exit(1);
                        }

                        if ((int) this.prob.x[i][0].value <= 0 || (int) this.prob.x[i][0].value > max_index) {
                            System.err.print("Wrong input format: sample_serial_number out of range\n");
                            System.exit(1);
                        }
                    }
                }

                fp.close();
                return;
            }

            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            vy.addElement(Double.valueOf(atof(st.nextToken())));
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];

            for (int j = 0; j < m; ++j) {
                x[j] = new svm_node();
                x[j].index = atoi(st.nextToken());
                x[j].value = atof(st.nextToken());
            }

            if (m > 0) {
                max_index = Math.max(max_index, x[m - 1].index);
            }

            vx.addElement(x);
        }
    }
}
