package com.calc;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.text.*;
import android.text.style.*;
import android.graphics.*;

public class MainActivity extends Activity 
{
	TextView textView;
	String sym = "\\s*(/|-|\\+|\\*|%|\\^)\\s*";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		textView = findViewById(R.id.mainTextViewCalc);
    }
	
	public double calcOperator(String operetor, double n1, double n2)
	{
		switch (operetor)
		{
			case "+":
				return n1 + n2;
				
			case "-":
				return n1 - n2;
			
			case "*":
				return n1 * n2;
				
			case "/":
				return n1 / n2;
				
			case "%":
				return n1 % n2;
				
			case "^":
				return Math.pow(n1, n2);
		}
		
		return 0;
	}
	
	public SpannableString makeSyntax(String text)
	{
		SpannableString spannableString = new SpannableString(text);

        String[] symbols = {"+", "-", "*", "/", "%", "^"};

        for (String symbol : symbols) {
            int start = text.indexOf(symbol);
            while (start >= 0) {
                int end = start + symbol.length();
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                start = text.indexOf(symbol, end);
            }
        }
		
		return spannableString;
	}
	
	public String calcText(String text)
	{
		String[] operations = text.split("\\s+");
		
		if (operations.length < 3) return text;
		
		double n1 = Double.parseDouble(operations[0]);
		String operator = operations[1];
		double n2 = Double.parseDouble(operations[2]);
		
		double result = calcOperator(operator, n1, n2);
		
		text = text.replaceFirst("^\\d+(\\.\\d+)?" + sym + "\\d+(\\.\\d+)?", result + "");
		
		return calcText(text);
	}
	
	public void onButtonClick(View view)
	{
		Button button = (Button) view;
		String text = textView.getText() + "";
		String chr = button.getText() + "";
		
		switch (chr)
		{
			case "DEL":
				text = text.replaceFirst("(\\.|\\d|" + sym + ")$", "");
				break;
				
			case "C":
				text = "";
				break;
				
			case "=":
				text = text.replaceFirst(" " + sym + " $", "");
				text = calcText(text);
				text = text.replaceFirst("\\.0", "");
				break;
		}
		
		if (chr.matches(sym))
		{
			text += " " + chr + " ";
			text = text.replaceFirst("(" + sym + "){2}$", " " + chr + " ");
		}

		if (chr.matches("[0-9.]"))
			text += chr;
			
		text = text.replaceAll("\\b0+([1-9]+)", "$1");
		text = text.replaceAll("[A-Za-z]+", "0");
		
		SpannableString syntax = makeSyntax(text);
		
		textView.setText(syntax);
	}
}
