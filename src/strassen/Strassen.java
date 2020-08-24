/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strassen;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 *
 * @author BJBP
 */
public class Strassen {
    /**
     * @param args the command line arguments
     */
private static int[][] multiplicacionStrassen(int[][] A, int[][] B) {
        int n = A.length;
        int[][] res = new int[n][n];
        // if the input matrix is 1x1
        if (n == 1) res[0][0] = A[0][0] * B[0][0];
        else {
            // first matrix
            int[][] a = new int[n / 2][n / 2];
            int[][] b = new int[n / 2][n / 2];
            int[][] c = new int[n / 2][n / 2];
            int[][] d = new int[n / 2][n / 2];
            
            // second matrix
            int[][] e = new int[n / 2][n / 2];
            int[][] f = new int[n / 2][n / 2];
            int[][] g = new int[n / 2][n / 2];
            int[][] h = new int[n / 2][n / 2];

            // dividing matrix A into 4 parts
            divideArray(A, a, 0, 0);
            divideArray(A, b, 0, n / 2);
            divideArray(A, c, n / 2, 0);
            divideArray(A, d, n / 2, n / 2);

            // dividing matrix B into 4 parts
            divideArray(B, e, 0, 0);
            divideArray(B, f, 0, n / 2);
            divideArray(B, g, n / 2, 0);
            divideArray(B, h, n / 2, n / 2);
            
            /** 
              p1 = (a + d)(e + h)
              p2 = (c + d)e
              p3 = a(f - h)
              p4 = d(g - e)
              p5 = (a + b)h
              p6 = (c - a) (e + f)
              p7 = (b - d) (g + h)
            **/
           
            int[][] p1 = multiplicacionStrassen(añadirMatrices(a, d), añadirMatrices(e, h));
            int[][] p2 = multiplicacionStrassen(añadirMatrices(c,d),e);
            int[][] p3 = multiplicacionStrassen(a, subMatrices(f, h));           
            int[][] p4 = multiplicacionStrassen(d, subMatrices(g, e));
            int[][] p5 = multiplicacionStrassen(añadirMatrices(a,b), h);
            int[][] p6 = multiplicacionStrassen(subMatrices(c, a), añadirMatrices(e, f));
            int[][] p7 = multiplicacionStrassen(subMatrices(b, d), añadirMatrices(g, h));

            
           /**
              C11 = p1 + p4 - p5 + p7
              C12 = p3 + p5
              C21 = p2 + p4
              C22 = p1 - p2 + p3 + p6
            **/
           
            int[][] C11 = añadirMatrices(subMatrices(añadirMatrices(p1, p4), p5), p7);
            int[][] C12 = añadirMatrices(p3, p5);
            int[][] C21 = añadirMatrices(p2, p4);
            int[][] C22 = añadirMatrices(subMatrices(añadirMatrices(p1, p3), p2), p6);

            // adding all subarray back into one
            copiarSubArray(C11, res, 0, 0);
            copiarSubArray(C12, res, 0, n / 2);
            copiarSubArray(C21, res, n / 2, 0);
            copiarSubArray(C22, res, n / 2, n / 2);
        }
        return res;
    }


    public static void main(String[] args) throws NumberFormatException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Ingrese orden de matrices:");
        int order = Integer.parseInt(br.readLine());

        int[][] A = new int[order][order];
        int[][] B = new int[order][order];
        
        int[][] res = new int[order][order];
        
        // input : first matrix
        System.out.println("Enter first matrix:");
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) 
                A[i][j] = Integer.parseInt(br.readLine());            
        }
        // input : second matrix
        System.out.println("Enter second matrix:");
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < order; j++) 
                B[i][j] = Integer.parseInt(br.readLine());            
        }

        res = multiplicacionStrassen(A, B);
        imprimirMatrix(res);
    }
    
    // helper methods
    
    // Adding 2 matrices
    public static int[][] añadirMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = a[i][j] + b[i][j];
            }
        }
        return res;
    }

    // Subtracting 2 matrices
    public static int[][] subMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = a[i][j] - b[i][j];
            }
        }
        return res;
    }

    // print matrix
    public static void imprimirMatrix(int[][] a) {
        int n = a.length;
        System.out.println("Resultant Matrix ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    // divides array
    public static void divideArray(int[][] P, int[][] C, int iB, int jB) 
    {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                C[i1][j1] = P[i2][j2];
    }

    // copies
    public static void copiarSubArray(int[][] C, int[][] P, int iB, int jB) 
    {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                P[i2][j2] = C[i1][j1];
    }  
    
}
