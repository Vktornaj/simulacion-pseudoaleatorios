package com.company;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Scanner;
import java.util.function.DoubleToIntFunction;

//Autor: Victor Eduardo Garcia Najera

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner Leer = new Scanner(System.in);
        int x0,k,c,g;
        double li, ls, media;
        System.out.print("Dame el valor inicial X0: ");
        x0 = Leer.nextInt();
        System.out.print("Dame el valor de k: ");
        k = Leer.nextInt();

        do {
            System.out.print("Dame el valor de c (Debe ser impar): ");
            c = Leer.nextInt();
        }while(c % 2 == 0);
        System.out.print("Dame el valor de g: ");
        g = Leer.nextInt();

        int nums[] = congLineal(x0, k , c , g);
        double numsD[] = pasFraccion(nums);

//        for (int i = 0; i < (int) Math.pow(2,g); i++) {
//            System.out.println("Numero " +  (i + 1) + ": " + nums[i]);
//        }
        int t = nums.length;
        if (nums[t - 1] == x0 && Math.pow(2,g) == t){
            System.out.println("Genera periodo completo");
        }else{
            System.out.println("No genera periodo completo");
        }

        media = media(numsD);
        System.out.println("Media: " + media);
        double z = calcularZ();
        //System.out.println(z);

        final double v = z * (1 / Math.sqrt((12 * t)));
        li = 0.5 - v;
        ls = 0.5 + v;

        System.out.println("Limite inferior: " + li);
        System.out.println("Limite superior: " + ls);

        if (pruebaMedias(media, li, ls)) {
            System.out.println("Pasa la prueba de medias");
        }else{
            System.out.println("Es rechazada por la prueba de medias ");
        }

        System.out.println("La varianza es: " + varianza(numsD));
//        int histo[] = histoG(numsD);
//        for (int i = 0; i < histo.length; i++) {
//            System.out.println("intervalo " + i + ": " + histo[i]);
//        }
        double chiCuadrado =  chiCua(numsD);
        System.out.println("Chi-cuadrado: " + chiCuadrado);

        //Prueba double abe[] = {1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1};
        independenciaN(numsD);
        pPoquer(numsD);

    }

    /*Metodo congruencial lineal*/
    public static int[] congLineal(int xi, int k, int c, int g) {
        int[] nums = new int[(int) Math.pow(2,g)];
        int a = 1 + 4 * k;
        int m = (int) Math.pow(2,g);
        for (int i = 0; i <= nums.length - 1; i++) {
            nums[i] = xi = (a * xi + c) % m;
            //nums [i] = (double) xi / (m - 1);
        }
        return nums;
    }

    public static double[] pasFraccion(int nums[]){
        double numsF[] = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            numsF[i] = (double) nums[i] / (nums.length - 1);
        }
        return numsF;
    }

    public static double media(double [] nums){
        double media = 0;
        for (int i = 0; i < nums.length; i++) {
            media += nums[i];
        }
        return media / nums.length;
    }

    public static double calcularZ(){
        NormalDistribution nd = new NormalDistribution();
        return nd.inverseCumulativeProbability(1 - 0.05 / 2);
    }

    public static boolean pruebaMedias(double media, double li, double ls){
        if (li < media && media < ls) {
            return true;
        }else{
            return false;
        }
    }

    public static double varianza(double nums[]){
        double media = 0, varianz = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            media =+ nums[i];
        }
        media = media / n;

        for (int i = 0; i < n; i++) {
            varianz = varianz + Math.pow(nums[i] - media,2);
        }
        return  varianz / (n - 1);
    }

    public static int[] histoG(double nums[]){

        int l = nums.length;
        int m = (int) Math.sqrt(l);
        int histo[];
        if (l % m != 0){
            histo = new int[m + 1];
        }else {
            histo = new int[m];
        }
        int intervalo = l/m;
        int cont = 0;
        //System.out.println("intervalo: " + intervalo);
        for (int i = 0; i < l; i = i + intervalo) {
            if (i + intervalo >= l){
                intervalo = l - i;
            }
            //System.out.println("intervaos: " + i + ", " + (i + intervalo));
            for (int j = 0; j < l; j++) {
                //
                // System.out.println("a: " + (i / (l - 1)) + " b: " + ((i + intervalo) / (l - 1)) + " n: " + nums[j]);
                if (nums[j] >= ((double)i / (l - 1)) && nums[j] < ((double)i + intervalo) / (l - 1)){
                    histo[cont]++;
                }
            }
            cont++;
        }
        return histo;
    }

    public static double chiCua(double numsD[]){
        int histo[] = histoG(numsD);
        int n = histo.length - 1;
        double fEsperada = numsD.length / Math.sqrt(numsD.length), suma = 0;
        for (int i = 0; i < n; i++) {
            suma = suma + Math.pow(histo[i] - fEsperada, 2) / fEsperada;
        }

        System.out.println("Grados de libertad: " + (n - 1));
        return suma;
    }

    public static boolean independenciaN(double[] numsD){
        int co = 1, n0 = 0, n1 = 0, n = numsD.length;
        double vEsperado, estadistico, varianza;
        boolean bandera;
        if (numsD[0] > 0.5 ){
            bandera = false;
        }else{
            bandera = true;
        }
        for (int i = 0; i < n; i++) {
            if (numsD[i] > 0.5) {
                n1++;
                if (bandera) {
                    co++;
                }
                bandera = false;
            }else{
                n0++;
                if (!bandera) {
                    co++;
                }
                bandera = true;
            }
        }
        System.out.println("co = " + co + ", n0 = " + n0 + ", n1 = " + n1 + ", n = " + n);
        vEsperado = 2.0 * n0 * n1 / n + 0.5;
        System.out.println("Esperado = " + vEsperado);
        double a = 2 * n0 * n1;
        varianza = (a * (a - n)) / (Math.pow(n, 2) * (n - 1));
        System.out.println("Varianza = " + varianza);
        estadistico = (co - vEsperado) / Math.sqrt(varianza);
        System.out.println("Z = " + estadistico);

        return (1.96F > estadistico && estadistico > -1.96F);
    }

    public static boolean pPoquer(double[] numsD){
        
        return true;
    }
}
