package co.tagtalk.winemate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import co.tagtalk.winemate.thriftfiles.WineMateServices;
import co.tagtalk.winemate.thriftfiles.WineReviewAndRatingWriteRequest;
import co.tagtalk.winemate.thriftfiles.WineReviewAndRatingWriteResponse;

public class WriteRatingAndReviewActivity extends AppCompatActivity {

    private Float ratingEntered;
    private Integer wineId;
    private String winePicURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_rating_and_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RatingBar ratingBar;
        final Button submitButton;
        final EditText reviewText;
        final TextView reviewCharCounter;

        Intent intent= getIntent();
        ratingEntered = intent.getFloatExtra("rating", 0);
        final Integer userId = intent.getIntExtra("userId", 0);
        wineId = intent.getIntExtra("wineId", 0);
        winePicURL = intent.getStringExtra("winePicURL");

        ratingBar = (RatingBar) findViewById(R.id.write_rating_and_review_rating_bar);

        if (ratingBar != null) {
            ratingBar.setRating(ratingEntered);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    ratingEntered = rating;
                }
            });
        }

        reviewText = (EditText) findViewById(R.id.write_rating_and_review_review_content);
        reviewCharCounter = (TextView) findViewById(R.id.write_rating_and_review_review_char_counter);

        if (reviewText != null) {
            reviewText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String counter = (1000 - s.length()) + "/1000";
                    if (reviewCharCounter != null) {
                        reviewCharCounter.setText(counter);
                    }
                }
            });
        }

        submitButton = (Button) findViewById(R.id.write_rating_and_review_submit_button);

        if (submitButton != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ratingEntered == 0) {
                        Toast.makeText(WriteRatingAndReviewActivity.this, R.string.rating_no_zero, Toast.LENGTH_SHORT).show();
                    } else {

                        String reviewContent = "";

                        if (reviewText != null) {

                            if (reviewText.getText().length() > 0 && reviewText.getText().length() < Configs.MINIMAL_REVIEW_CONTENT_CHARS) {

                                Toast.makeText(WriteRatingAndReviewActivity.this, R.string.review_less_than_ten_chars, Toast.LENGTH_SHORT).show();
                                return;

                            } else if(reviewText.getText().length() > Configs.MINIMAL_REVIEW_CONTENT_CHARS) {
                                reviewContent = reviewText.getText().toString();
                            }
                        }
                        TimeStamp timeStamp = new TimeStamp();
                        WineReviewAndRatingWriteRequest wineReviewAndRatingWriteRequest = new WineReviewAndRatingWriteRequest(wineId, userId, ratingEntered, reviewContent, timeStamp.getCurrentDate(), timeStamp.getCurrentTime());
                        final WriteWineReviewAndRatingTask writeWineReviewAndRatingTask = new WriteWineReviewAndRatingTask(WriteRatingAndReviewActivity.this);
                        writeWineReviewAndRatingTask.execute(wineReviewAndRatingWriteRequest);
                    }
                }
            });
        }
    }



    public class WriteWineReviewAndRatingTask extends AsyncTask<WineReviewAndRatingWriteRequest, Void, WineReviewAndRatingWriteResponse> {

        private Activity activity;
        private boolean gotException;

        public WriteWineReviewAndRatingTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected WineReviewAndRatingWriteResponse doInBackground(WineReviewAndRatingWriteRequest... params) {
            TTransport transport = new TSocket(Configs.SERVER_ADDRESS, Configs.PORT_NUMBER);
            WineReviewAndRatingWriteResponse wineReviewAndRatingWriteResponse = new WineReviewAndRatingWriteResponse();

            try{

                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                WineMateServices.Client client = new WineMateServices.Client(protocol);

                wineReviewAndRatingWriteResponse = client.writeWineReviewAndRating(params[0]);


            } catch (TException x) {
                x.printStackTrace();
                gotException = true;
            }
            transport.close();

            return wineReviewAndRatingWriteResponse;
        }

        @Override
        protected void onPostExecute(WineReviewAndRatingWriteResponse wineReviewAndRatingWriteResponse) {
            super.onPostExecute(wineReviewAndRatingWriteResponse);

            if (gotException) {
                Toast.makeText(this.activity, R.string.rating_review_write_fail, Toast.LENGTH_LONG).show();
                return;
            }

            if (!wineReviewAndRatingWriteResponse.isSuccess) {
                Toast.makeText(this.activity, R.string.rating_review_write_fail, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.activity, R.string.rating_review_write_success, Toast.LENGTH_SHORT).show();
                Intent intent= new Intent();
                intent.putExtra("wineId", wineId);
                intent.putExtra("winePicURL", winePicURL);
                intent.setClassName("co.tagtalk.winemate",
                        "co.tagtalk.winemate.WineInfoActivity");
                startActivity(intent);
                finish();
            }
        }
    }

 }
