package com.m_alrajab.android.marketeye.model.rest;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m_alrajab.android.marketeye.R;
import com.m_alrajab.android.marketeye.controller.touch_helper.ItemTouchHelperAdapter;
import com.m_alrajab.android.marketeye.model.db.QuoteColumns;
import com.m_alrajab.android.marketeye.model.db.QuoteProvider;
import com.m_alrajab.android.marketeye.utils.Utils;

/**
 * Created by m_alrajab on 8/20/16.
 *
 */
public class QuoteCursorAdapter extends CursorRecyclerViewAdapter<ViewHolder>
    implements ItemTouchHelperAdapter{

  private static Context mContext;
  private static Typeface robotoLight;
  private boolean isPercent;
  public QuoteCursorAdapter(Context context, Cursor cursor){
    super(context, cursor);
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    robotoLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Light.ttf");
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_quote, parent, false);
    ViewHolder vh = new ViewHolder(itemView);
    return vh;
  }

  @Override
  public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor){
    viewHolder.symbol.setText(cursor.getString(cursor.getColumnIndex("symbol")));
    viewHolder.bidPrice.setText(cursor.getString(cursor.getColumnIndex("bid_price")));
    int sdk = Build.VERSION.SDK_INT;
    if (cursor.getInt(cursor.getColumnIndex("is_up")) == 1){
      if (sdk < Build.VERSION_CODES.JELLY_BEAN){
        viewHolder.change.setBackgroundDrawable(
            mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
      }else {
        viewHolder.change.setBackground(
            mContext.getResources().getDrawable(R.drawable.percent_change_pill_green));
      }
    } else{
      if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
        viewHolder.change.setBackgroundDrawable(
            mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
      } else{
        viewHolder.change.setBackground(
            mContext.getResources().getDrawable(R.drawable.percent_change_pill_red));
      }
    }
    if (Utils.showPercent){
      viewHolder.change.setText(cursor.getString(cursor.getColumnIndex("percent_change")));
    } else{
      viewHolder.change.setText(cursor.getString(cursor.getColumnIndex("change")));
    }



  }

  @Override public void onItemDismiss(int position) {
    Cursor c = getCursor();
    c.moveToPosition(position);
    String symbol = c.getString(c.getColumnIndex(QuoteColumns.SYMBOL));
    mContext.getContentResolver().delete(QuoteProvider.Quotes.withSymbol(symbol), null, null);
    notifyItemRemoved(position);
  }

  @Override public int getItemCount() {
    return super.getItemCount();
  }


}
