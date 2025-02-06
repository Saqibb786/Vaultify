package com.example.bankingproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private List<FAQ> faqList;

    // Constructor
    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    @Override
    public FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_item, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FAQViewHolder holder, int position) {
        FAQ faq = faqList.get(position);
        holder.questionText.setText(faq.getQuestion());
        holder.answerText.setText(faq.getAnswer());
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {

        TextView questionText, answerText;
        CardView cardView;

        public FAQViewHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            answerText = itemView.findViewById(R.id.answer_text);
            cardView = itemView.findViewById(R.id.cardviewFAQ);
        }
    }
}
