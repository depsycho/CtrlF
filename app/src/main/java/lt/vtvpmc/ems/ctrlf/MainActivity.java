package lt.vtvpmc.ems.ctrlf;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button ButtonCopyText;
    private Button ButtonFindPhrase;
    private Button ButtonReset;
    private TextView TextViewFoundPhrase;
    private TextView TextViewCopiedText;

    private String initialText;
    private String phrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonCopyText = (Button) findViewById(R.id.ButtonCopyText);
        ButtonFindPhrase = (Button) findViewById(R.id.ButtonFindPhrase);
        ButtonReset = (Button) findViewById(R.id.ButtonReset);
        TextViewFoundPhrase = (TextView) findViewById(R.id.TextViewFoundPhrase);
        TextViewCopiedText = (TextView) findViewById(R.id.TextViewCopiedText);

        ButtonCopyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initial clipboard service
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String clip = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();

                // Get text String from clipboard and paste to text view field
                if (clip != null) {
                    TextViewCopiedText.setText(clip);
                    initialText = TextViewCopiedText.getText().toString();
                } else {
                    TextViewFoundPhrase.setText("Sorry, clipboard is empty");
                }
            }
        });

        ButtonFindPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextViewCopiedText.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Find phrase");

                    final EditText editTextPhrase = new EditText(MainActivity.this);
                    editTextPhrase.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(editTextPhrase);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            phrase = editTextPhrase.getText().toString();

                            if (phrase.length() > 0) {
                                TextViewFoundPhrase.setText(findPhrase());
                            } else {
                                TextViewCopiedText.setText(initialText);
                                TextViewFoundPhrase.setText("How I can find nothing?");
                            }
                        }
                    });
                    {
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                    }
                    builder.show();

                } else {
                    TextViewFoundPhrase.setText("Copy text from clipboard text first");
                }
            }
        });

        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewCopiedText.setText("");
                TextViewFoundPhrase.setText("");
            }
        });


    }

    private String findPhrase() {
        SpannableString spannableString = new SpannableString(initialText);
        int phraseLength = phrase.length();
        int index = initialText.indexOf(phrase);
        int phraseCounter = 0;
        while (index >= 0) {
            phraseCounter += 1;
            spannableString.setSpan(new BackgroundColorSpan(Color.argb(255, 152, 238, 152)), index, index + phraseLength, 0);
            index = initialText.indexOf(phrase, index + phraseLength);
        }
        TextViewCopiedText.setText(spannableString);
        return String.format("I found %d phrases match", phraseCounter);
    }
}
