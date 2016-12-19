package com.example.nayra.studyjamcalculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    private  TextView txvUno,txvDos,txvTres,txvCuatro,txvCinco,txvSeis,txvSiete,txvOcho,txvNueve,txvCero,txvIgual,txvSumar,txvRestar,txvMultiplicar,txvDividir,txvCE,txvComa,txvResultado;
    private  String expresion="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvUno=(TextView)findViewById(R.id.uno);
        txvDos=(TextView)findViewById(R.id.dos);
        txvTres=(TextView)findViewById(R.id.tres);
        txvCuatro=(TextView)findViewById(R.id.cuatro);
        txvCinco=(TextView)findViewById(R.id.cinco);
        txvSeis=(TextView)findViewById(R.id.seis);
        txvSiete=(TextView)findViewById(R.id.siete);
        txvOcho=(TextView)findViewById(R.id.ocho);
        txvNueve=(TextView)findViewById(R.id.nueve);
        txvCero=(TextView)findViewById(R.id.cero);

        txvSumar=(TextView)findViewById(R.id.sumar);
        txvRestar=(TextView)findViewById(R.id.restar);
        txvMultiplicar=(TextView)findViewById(R.id.multiplicar);
        txvDividir=(TextView)findViewById(R.id.division);

        txvComa=(TextView)findViewById(R.id.coma);
        txvCE=(TextView)findViewById(R.id.ce);

        txvResultado=(TextView)findViewById(R.id.resultado);



    }

    public void CrearCadena(View v)
    {
        TextView [] vecNum={txvUno,txvDos,txvTres,txvCuatro,txvCinco,txvSeis,txvSiete,txvOcho,txvNueve,txvCero,txvSumar,txvRestar,txvMultiplicar,txvDividir};
        // TextView [] vecNum={txvUno};

        for (int i=0;i<vecNum.length;i++ )
        {
            if (v.getId()==vecNum[i].getId())
            {
                expresion= expresion+vecNum[i].getText()+"";
            }
        }
        MostrarExpresion(expresion);


    }

    private  void MostrarExpresion(String expresion) {

        txvResultado.setText(expresion);

    }

    public  void CE(View v)
    {
        if(!expresion.equals(""))
        {
            expresion=expresion.substring(0,expresion.length()-1);
            MostrarExpresion(expresion);
        }

    }

    public void calcular(View v)
    {
        //Depurar la expresion algebraica
        String expr = depurar(expresion);
        String[] arrayInfix = expr.split(" ");

        //DeclaraciÃ³n de las pilas
        Stack< String > E = new Stack < String > (); //Pila entrada
        Stack < String > P = new Stack < String > (); //Pila temporal para operadores
        Stack < String > S = new Stack < String > (); //Pila salida

        //AÃ±adir la array a la Pila de entrada (E)
        for (int i = arrayInfix.length - 1; i >= 0; i--) {
            E.push(arrayInfix[i]);
        }

        try {
            //Algoritmo Infijo a Postfijo
            while (!E.isEmpty()) {
                switch (pref(E.peek())){
                    case 1:
                        P.push(E.pop());
                        break;
                    case 3:
                    case 4:
                        while(pref(P.peek()) >= pref(E.peek())) {
                            S.push(P.pop());
                        }
                        P.push(E.pop());
                        break;
                    case 2:
                        while(!P.peek().equals("(")) {
                            S.push(P.pop());
                        }
                        P.pop();
                        E.pop();
                        break;
                    default:
                        S.push(E.pop());
                }
            }

            //Eliminacion de `impurezasÂ´ en la expresiones algebraicas
            String infix = expr.replace(" ", "");
            String postfix = S.toString().replaceAll("[\\]\\[,]", "");

            //Mostrar resultados:
            String[] post = postfix.split(" ");
            calcular1(post);

        }catch(Exception ex){
            System.out.println("Error en la expresiÃ³n algebraica");
            System.err.println(ex);
        }
    }

    //Depurar expresiÃ³n algebraica
    private static String depurar(String s) {
        s = s.replaceAll("\\s+", ""); //Elimina espacios en blanco
        s = "(" + s + ")";
        String simbols = "+-x/()";
        String str = "";

        //Deja espacios entre operadores
        for (int i = 0; i < s.length(); i++) {
            if (simbols.contains("" + s.charAt(i))) {
                str += " " + s.charAt(i) + " ";
            }else str += s.charAt(i);
        }
        return str.replaceAll("\\s+", " ").trim();
    }

    //Jerarquia de los operadores
    private static int pref(String op) {
        int prf = 99;
        if (op.equals("^")) prf = 5;
        if (op.equals("x") || op.equals("/")) prf = 4;
        if (op.equals("+") || op.equals("-")) prf = 3;
        if (op.equals(")")) prf = 2;
        if (op.equals("(")) prf = 1;
        return prf;
    }

    public void calcular1(String[] cad)
    {
        //DeclaraciÃ³n de las pilas
        Stack < String > E = new Stack < String > (); //Pila entrada
        Stack < String > P = new Stack < String > (); //Pila de operandos

        //AÃ±adir post (array) a la Pila de entrada (E)
        for (int i = cad.length - 1; i >= 0; i--) {
            E.push(cad[i]);
        }

        //Algoritmo de EvaluaciÃ³n Postfija
        String operadores = "+-x/";
        while (!E.isEmpty()) {
            if (operadores.contains("" + E.peek())) {
                P.push(evaluar(E.pop(), P.pop(), P.pop()) + "");
            }else {
                P.push(E.pop());
            }
        }

       /* //Mostrar resultados:
        System.out.println("Expresion: " + expr);
        System.out.println("Resultado: " + P.peek());*/

        if( Double.parseDouble(P.peek()) % 1 == 0 )
        {
            P.push(P.peek().substring(0, P.peek().length() - 2));
            expresion = P.peek();
            MostrarExpresion(P.peek());
        }
        else
        {
            double numero = Math.rint(Double.parseDouble(P.peek())*100)/100;
            //Toast.makeText(getApplicationContext(),numero+"",Toast.LENGTH_LONG).show();
            expresion=numero+"";
            MostrarExpresion(numero+"");
        }

    }

    private static double evaluar(String op, String n2, String n1) {
        double num1 = Double.parseDouble(n1);
        double num2 = Double.parseDouble(n2);
        if (op.equals("+")) return (num1 + num2);
        if (op.equals("-")) return (num1 - num2);
        if (op.equals("x")) return (num1 * num2);
        if (op.equals("/")) return (num1 / num2);
        return 0;
    }


}
