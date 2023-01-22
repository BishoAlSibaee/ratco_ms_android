package com.syrsoft.ratcoms.PROJECTSActivity.ADAPTER;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.syrsoft.ratcoms.PROJECTSActivity.AcceptLocalPurchaseOrder;
import com.syrsoft.ratcoms.PROJECTSActivity.MyLocalPurchaseOrders;
import com.syrsoft.ratcoms.PROJECTSActivity.VeiwPurchaseOrder;
import com.syrsoft.ratcoms.R ;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syrsoft.ratcoms.PROJECTSActivity.LOCAL_PURCHASE_ORDER;

import java.util.List;

public class PurchaseOrders_Adapter extends RecyclerView.Adapter<PurchaseOrders_Adapter.HOLDER> {

    List<LOCAL_PURCHASE_ORDER> list ;

    public PurchaseOrders_Adapter(List<LOCAL_PURCHASE_ORDER> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public PurchaseOrders_Adapter.HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_purchase_order_unit,parent,false);
        HOLDER holder = new HOLDER(V);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseOrders_Adapter.HOLDER holder, int position) {
        holder.num.setText(String.valueOf(list.get(position).getId()));
        holder.name.setText(list.get(position).getProjectName());
        if (holder.itemView.getContext().getClass().getName().equals("com.syrsoft.ratcoms.PROJECTSActivity.AcceptLocalPurchaseOrder")) {
            if (AcceptLocalPurchaseOrder.ApproveUsers.size() == 5) {
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(4) != null) {
                    holder.name5.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(4).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(4).LastName);
                    holder.res5.setText(list.get(position).getAcc5Res());
                    holder.name5.setVisibility(View.VISIBLE);
                    holder.res5.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc5Res().equals("accepted")) {
                        holder.res5.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc5Res().equals("pending")) {
                        holder.res5.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc5Res().equals("rejected")) {
                        holder.res5.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(3) != null) {
                    holder.name4.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(3).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(3).LastName);
                    holder.res4.setText(list.get(position).getAcc4Res());
                    holder.name4.setVisibility(View.VISIBLE);
                    holder.res4.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc4Res().equals("accepted")) {
                        holder.res4.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc4Res().equals("pending")) {
                        holder.res4.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc4Res().equals("rejected")) {
                        holder.res4.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(2) != null) {
                    holder.name3.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(2).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(2).LastName);
                    holder.res3.setText(list.get(position).getAcc3Res());
                    holder.name3.setVisibility(View.VISIBLE);
                    holder.res3.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc3Res().equals("accepted")) {
                        holder.res3.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc3Res().equals("pending")) {
                        holder.res3.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc3Res().equals("rejected")) {
                        holder.res3.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(1) != null) {
                    holder.name2.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(1).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(0) != null) {
                    holder.name1.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(0).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (AcceptLocalPurchaseOrder.ApproveUsers.size() == 4) {
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(3) != null) {
                    holder.name4.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(3).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(3).LastName);
                    holder.res4.setText(list.get(position).getAcc4Res());
                    holder.name4.setVisibility(View.VISIBLE);
                    holder.res4.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc4Res().equals("accepted")) {
                        holder.res4.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc4Res().equals("pending")) {
                        holder.res4.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc4Res().equals("rejected")) {
                        holder.res4.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(2) != null) {
                    holder.name3.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(2).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(2).LastName);
                    holder.res3.setText(list.get(position).getAcc3Res());
                    holder.name3.setVisibility(View.VISIBLE);
                    holder.res3.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc3Res().equals("accepted")) {
                        holder.res3.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc3Res().equals("pending")) {
                        holder.res3.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc3Res().equals("rejected")) {
                        holder.res3.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(1) != null) {
                    holder.name2.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(1).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(0) != null) {
                    holder.name1.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(0).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (AcceptLocalPurchaseOrder.ApproveUsers.size() == 3) {
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(2) != null) {
                    holder.name3.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(2).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(2).LastName);
                    holder.res3.setText(list.get(position).getAcc3Res());
                    holder.name3.setVisibility(View.VISIBLE);
                    holder.res3.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc3Res().equals("accepted")) {
                        holder.res3.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc3Res().equals("pending")) {
                        holder.res3.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc3Res().equals("rejected")) {
                        holder.res3.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(1) != null) {
                    holder.name2.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(1).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(0) != null) {
                    holder.name1.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(0).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (AcceptLocalPurchaseOrder.ApproveUsers.size() == 2) {
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(1) != null) {
                    holder.name2.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(1).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(0) != null) {
                    holder.name1.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(0).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (AcceptLocalPurchaseOrder.ApproveUsers.size() == 1) {
                if (AcceptLocalPurchaseOrder.ApproveUsers.get(0) != null) {
                    holder.name1.setText(AcceptLocalPurchaseOrder.ApproveUsers.get(0).FirstName + " " + AcceptLocalPurchaseOrder.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
        }
        else if (holder.itemView.getContext().getClass().getName().equals("com.syrsoft.ratcoms.PROJECTSActivity.MyLocalPurchaseOrders")) {
            if (MyLocalPurchaseOrders.ApproveUsers.size() == 5) {
                if (MyLocalPurchaseOrders.ApproveUsers.get(4) != null) {
                    holder.name5.setText(MyLocalPurchaseOrders.ApproveUsers.get(4).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(4).LastName);
                    holder.res5.setText(list.get(position).getAcc5Res());
                    holder.name5.setVisibility(View.VISIBLE);
                    holder.res5.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc5Res().equals("accepted")) {
                        holder.res5.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc5Res().equals("pending")) {
                        holder.res5.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc5Res().equals("rejected")) {
                        holder.res5.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(3) != null) {
                    holder.name4.setText(MyLocalPurchaseOrders.ApproveUsers.get(3).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(3).LastName);
                    holder.res4.setText(list.get(position).getAcc4Res());
                    holder.name4.setVisibility(View.VISIBLE);
                    holder.res4.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc4Res().equals("accepted")) {
                        holder.res4.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc4Res().equals("pending")) {
                        holder.res4.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc4Res().equals("rejected")) {
                        holder.res4.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(2) != null) {
                    holder.name3.setText(MyLocalPurchaseOrders.ApproveUsers.get(2).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(2).LastName);
                    holder.res3.setText(list.get(position).getAcc3Res());
                    holder.name3.setVisibility(View.VISIBLE);
                    holder.res3.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc3Res().equals("accepted")) {
                        holder.res3.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc3Res().equals("pending")) {
                        holder.res3.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc3Res().equals("rejected")) {
                        holder.res3.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(1) != null) {
                    holder.name2.setText(MyLocalPurchaseOrders.ApproveUsers.get(1).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(0) != null) {
                    holder.name1.setText(MyLocalPurchaseOrders.ApproveUsers.get(0).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (MyLocalPurchaseOrders.ApproveUsers.size() == 4) {
                if (MyLocalPurchaseOrders.ApproveUsers.get(3) != null) {
                    holder.name4.setText(MyLocalPurchaseOrders.ApproveUsers.get(3).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(3).LastName);
                    holder.res4.setText(list.get(position).getAcc4Res());
                    holder.name4.setVisibility(View.VISIBLE);
                    holder.res4.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc4Res().equals("accepted")) {
                        holder.res4.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc4Res().equals("pending")) {
                        holder.res4.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc4Res().equals("rejected")) {
                        holder.res4.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(2) != null) {
                    holder.name3.setText(MyLocalPurchaseOrders.ApproveUsers.get(2).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(2).LastName);
                    holder.res3.setText(list.get(position).getAcc3Res());
                    holder.name3.setVisibility(View.VISIBLE);
                    holder.res3.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc3Res().equals("accepted")) {
                        holder.res3.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc3Res().equals("pending")) {
                        holder.res3.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc3Res().equals("rejected")) {
                        holder.res3.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(1) != null) {
                    holder.name2.setText(MyLocalPurchaseOrders.ApproveUsers.get(1).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(0) != null) {
                    holder.name1.setText(MyLocalPurchaseOrders.ApproveUsers.get(0).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (MyLocalPurchaseOrders.ApproveUsers.size() == 3) {
                if (MyLocalPurchaseOrders.ApproveUsers.get(2) != null) {
                    holder.name3.setText(MyLocalPurchaseOrders.ApproveUsers.get(2).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(2).LastName);
                    holder.res3.setText(list.get(position).getAcc3Res());
                    holder.name3.setVisibility(View.VISIBLE);
                    holder.res3.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc3Res().equals("accepted")) {
                        holder.res3.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc3Res().equals("pending")) {
                        holder.res3.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc3Res().equals("rejected")) {
                        holder.res3.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(1) != null) {
                    holder.name2.setText(MyLocalPurchaseOrders.ApproveUsers.get(1).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(0) != null) {
                    holder.name1.setText(MyLocalPurchaseOrders.ApproveUsers.get(0).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (MyLocalPurchaseOrders.ApproveUsers.size() == 2) {
                if (MyLocalPurchaseOrders.ApproveUsers.get(1) != null) {
                    holder.name2.setText(MyLocalPurchaseOrders.ApproveUsers.get(1).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(1).LastName);
                    holder.res2.setText(list.get(position).getAcc2Res());
                    holder.name2.setVisibility(View.VISIBLE);
                    holder.res2.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc2Res().equals("accepted")) {
                        holder.res2.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc2Res().equals("pending")) {
                        holder.res2.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc2Res().equals("rejected")) {
                        holder.res2.setTextColor(Color.RED);
                    }
                }
                if (MyLocalPurchaseOrders.ApproveUsers.get(0) != null) {
                    holder.name1.setText(MyLocalPurchaseOrders.ApproveUsers.get(0).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
            if (MyLocalPurchaseOrders.ApproveUsers.size() == 1) {
                if (MyLocalPurchaseOrders.ApproveUsers.get(0) != null) {
                    holder.name1.setText(MyLocalPurchaseOrders.ApproveUsers.get(0).FirstName + " " + MyLocalPurchaseOrders.ApproveUsers.get(0).LastName);
                    holder.res1.setText(list.get(position).getAcc1Res());
                    holder.name1.setVisibility(View.VISIBLE);
                    holder.res1.setVisibility(View.VISIBLE);
                    if (list.get(position).getAcc1Res().equals("accepted")) {
                        holder.res1.setTextColor(holder.itemView.getResources().getColor(R.color.teal_700));
                    } else if (list.get(position).getAcc1Res().equals("pending")) {
                        holder.res1.setTextColor(Color.BLUE);
                    } else if (list.get(position).getAcc1Res().equals("rejected")) {
                        holder.res1.setTextColor(Color.RED);
                    }
                }
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), VeiwPurchaseOrder.class);
                i.putExtra("Index" , position);
                i.putExtra("Source" , holder.itemView.getContext().getClass().getName());
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HOLDER extends RecyclerView.ViewHolder {
        TextView num , name ;
        TextView name1 , name2 , name3 , name4 , name5 ,res1 , res2 , res3 , res4, res5;

        public HOLDER(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.textView117);
            name = itemView.findViewById(R.id.textView118);
            name1 = itemView.findViewById(R.id.textView133);
            name2 = itemView.findViewById(R.id.textView131);
            name3 = itemView.findViewById(R.id.textView129);
            name4 = itemView.findViewById(R.id.textView127);
            name5 = itemView.findViewById(R.id.textView125);
            res1 = itemView.findViewById(R.id.textView132);
            res2 = itemView.findViewById(R.id.textView130);
            res3 = itemView.findViewById(R.id.textView128);
            res4 = itemView.findViewById(R.id.textView126);
            res5 = itemView.findViewById(R.id.textView124);
            name1.setVisibility(View.GONE);
            name2.setVisibility(View.GONE);
            name3.setVisibility(View.GONE);
            name4.setVisibility(View.GONE);
            name5.setVisibility(View.GONE);
            res1.setVisibility(View.GONE);
            res2.setVisibility(View.GONE);
            res3.setVisibility(View.GONE);
            res4.setVisibility(View.GONE);
            res5.setVisibility(View.GONE);
        }
    }
}
