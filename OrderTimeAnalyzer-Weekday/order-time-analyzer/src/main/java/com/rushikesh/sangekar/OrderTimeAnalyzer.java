package com.rushikesh.sangekar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
// import org.json.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderTimeAnalyzer {
    public static final int TOT_SLOTS = 7;
    public static boolean[] getDeliveryTime(Queue<Order> q){
        double currentTime = 0;
        int availableSlots = TOT_SLOTS;
        boolean[] unDeliv = new boolean[100];
        PriorityQueue<Order> pq = new PriorityQueue<Order>(availableSlots, new Comparator<Order>(){
            public int compare(Order o1, Order o2){
                if(o1.getTotTime() > o2.getTotTime())
                    return 1;
                else if (o1.getTotTime() < o2.getTotTime())
                    return -1;
                else
                    return 0;
            }
        });
        Order tempOrd;
        while(q.size()>0){
            if(q.peek().getRequiredSlots()>OrderTimeAnalyzer.TOT_SLOTS){
                unDeliv[q.poll().id] = true;
            }else if(availableSlots>=q.peek().getRequiredSlots()){
                q.peek().setStartTime(currentTime);
                if(q.peek().getTotTime()<=150){
                    availableSlots -= q.peek().getRequiredSlots();
                    pq.add(q.poll());
                }else{
                    unDeliv[q.poll().id] = true;
                }
            }else{
                tempOrd = pq.poll();
                currentTime = tempOrd.getTotTime();
                availableSlots += tempOrd.getRequiredSlots();
            }
        }
        return unDeliv;
    }
    public static Queue<Order> formatInput() throws Exception{
        Queue<Order> inpQueue = new ArrayDeque<Order>();
        int ordId, mcnt, acnt;
        double dist;
        try{
            BufferedReader br = new BufferedReader(new FileReader("input.txt"));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
    
                    line = br.readLine();
                }
                String jsonInp = sb.toString();
                JSONArray jsArr = new JSONArray(jsonInp);
                JSONArray mealsArr;
                Order ord;
                for(int i = 0 ; i < jsArr.length() ; i++){
                    mcnt = 0;
                    acnt = 0;
                    ordId = jsArr.getJSONObject(i).getInt("orderId");
                    dist = jsArr.getJSONObject(i).getDouble("distance");
                    mealsArr = jsArr.getJSONObject(i).getJSONArray("meals");
                    for(int j=0;j<mealsArr.length();j++){
                        if(mealsArr.getString(j).equals("M"))
                            mcnt++;
                        else
                            acnt++;
                    }
                    ord = new Order(ordId, mcnt, acnt, dist);
                    inpQueue.add(ord);
                }
                System.out.println(inpQueue);
            }catch(Exception e){
                System.out.println(e);
            }finally{
                br.close();
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found "+e);
        }
        return inpQueue;
    }
    public static void main(String[] args){
        
        // boolean[] unDeliv = new boolean[100];
        Queue<Order> inputQueue;
        StringBuilder sb = new StringBuilder();
        try{
            inputQueue = formatInput();
            boolean[] unDeliv = getDeliveryTime(new LinkedList<Order>(inputQueue));
            Order ord;
            while(inputQueue.size()>0){
                ord = inputQueue.poll();
                if(!unDeliv[ord.id])
                    System.out.println("Order "+ord.id+" will get delivered in "+ord.getTotTime()+" minutes");
                else
                    System.out.println("Order "+ord.id+" is denied because the restaurant cannot accommodate it.");

            }
        }catch(Exception e){
            System.out.println("Failed to read input "+e);
        }
        
    }
}
