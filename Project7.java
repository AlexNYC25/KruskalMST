import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.Scanner;


class edgeNode{

    int Ni;
    int Nj;
    int cost;

    edgeNode next;
 
    public edgeNode(int ni, int nj, int cost){
        this.Ni = ni;
        this.Nj = nj;
        this.cost = cost;

        this.next = null;
    }

    public void printNode(){
        System.out.println("Node Ni:" + this.Ni + " Node Nj" + this.Nj + " Cost:" + this.cost);

    }

}

class KruskalMST{
    int numNodes;
    int[] inWhichSet;
    int numSets;
    int totalMSTCost;

    edgeNode listHeadEdge;
    edgeNode listHeadMST;

    public KruskalMST(int numNodes){
        this.numNodes = numNodes;
        this.numSets = this.numNodes;
        this.inWhichSet = new int[this.numNodes + 1];

        this.totalMSTCost = 0;

        this.listHeadEdge = new edgeNode(0, 0, 0);
        this.listHeadMST = new edgeNode(0, 0, 0);

        for(int x = 1; x < this.numNodes + 1; x++){
            this.inWhichSet[x] = x;
        }

    }

    public void insert(edgeNode node, edgeNode listHead){

        edgeNode pointer = listHead;
        
        // case for when we reach the end of linked list
        if(pointer.next == null){
            pointer.next = node;
        }
        else {
            while(pointer.next != null && pointer.next.cost < node.cost){
                pointer = pointer.next;
            }

            edgeNode temp = pointer.next;

            pointer.next = node;
            node.next = temp;
        }
    }

    public edgeNode removeEdge(edgeNode listHead){

        // list has dummy node as head so the
        // first real node is the node after the dummy
        edgeNode pointer = listHead.next;

        while(true){
            /*
                we check if the edge is valid in the function
                otherwise for some reason nullpoint error occurs
            */
            if(inWhichSet[pointer.Ni] != inWhichSet[pointer.Nj]){
                edgeNode temp = new edgeNode(pointer.Ni, pointer.Nj, pointer.cost);
                return temp;
            }
            else {
                pointer = pointer.next;
            }
        }



    }

    public void merge2Sets(int node1, int node2){
        if(inWhichSet[node1] < inWhichSet[node2]){
            int temp = inWhichSet[node2];

            for(int x = 1; x < this.numNodes + 1; x++){
                if(inWhichSet[x] == temp){
                    inWhichSet[x] = inWhichSet[node1];
                }
            }
        }
        else{
            int temp = inWhichSet[node1];

            for(int x = 1; x < this.numNodes + 1; x++){
                if(inWhichSet[x] == temp){
                    inWhichSet[x] = inWhichSet[node2];
                }
            }
        }

    }

    public void printListDebug(edgeNode listHead){
        edgeNode temp = listHead;

        while(temp != null){
            if(temp.next == null){
                System.out.println("<"+temp.Ni+","+temp.Nj+","+temp.cost+">\n");
            }
            else{
                System.out.println("<"+temp.Ni+","+temp.Nj+","+temp.cost+"> ");  

            }
            temp = temp.next;
        }
    }

    public void printList(edgeNode listHead, BufferedWriter file){
        
        edgeNode temp = listHead;

        try{
            while(temp != null){
                if(temp.next == null){
                    file.write("<"+temp.Ni+","+temp.Nj+","+temp.cost+">\n");
                }
                else{
                    file.write("<"+temp.Ni+","+temp.Nj+","+temp.cost+"> ");  

                }
                temp = temp.next;
            }

            file.write("\n");

        } catch (Exception e){
            e.toString();
        }


    }

    public void printArr(BufferedWriter file){

        try{
            file.write("Contents of InWhichSet Array : \n");

            for(int x = 0 ; x < inWhichSet.length; x++){
                file.write(" InWhichSet[" + x + "]: " + inWhichSet[x]);
            }

            file.write("\n");
        } catch (Exception e){

        }
        
        
    }


}

public class Project7{

    public static void main(String[] args) {

        System.out.println("started program");

        Scanner inFile = null;
        BufferedWriter mstFile = null;
        BufferedWriter debugFile = null;
 
        try {
            inFile = new Scanner( new BufferedReader(new FileReader(args[0])));
            mstFile = new BufferedWriter( new FileWriter(args[1]));
            debugFile = new BufferedWriter( new FileWriter(args[2]));
        } catch (Exception e){
            System.out.println("Error in file object creation");
        }

        int numOfNodesInFile = inFile.nextInt();
    

        KruskalMST graph = new KruskalMST(numOfNodesInFile);
        System.out.println("finished creating graph object");
        // print ary
        graph.printArr(debugFile);

        edgeNode tempInsert;
        while(inFile.hasNext()){
            int ni = inFile.nextInt();
            int nj = inFile.nextInt();
            int cost = inFile.nextInt();

            tempInsert = new edgeNode(ni, nj, cost);

            System.out.println("created new node: " + tempInsert.Ni + " " + tempInsert.Nj + " " + tempInsert.cost);

            graph.insert(tempInsert, graph.listHeadEdge);

            // print list
            graph.printListDebug(graph.listHeadEdge);
            try{
                debugFile.write("Contents of list head edge linked list: ");
            } catch (Exception e){

            }
            
            graph.printList(graph.listHeadEdge, debugFile);
        }

        System.out.println("finished reading values");
        
        

        edgeNode nextEdge;
        while(graph.numSets > 1){
            System.out.println("in heart of program num sets " + graph.numSets);

            do{
                nextEdge = graph.removeEdge(graph.listHeadEdge);
            }while(graph.inWhichSet[nextEdge.Ni] == graph.inWhichSet[nextEdge.Nj]);      

            
            graph.insert(nextEdge, graph.listHeadMST);
            graph.totalMSTCost += nextEdge.cost;
            graph.merge2Sets(nextEdge.Ni, nextEdge.Nj);
            graph.numSets--;
            
            

            // print Ary (inWhichSet)
            graph.printArr(debugFile);

            
            try{
                // graph.printList(ListHeadMst)
                debugFile.write("Printing out contents of List Head MST Linked List");
                graph.printList(graph.listHeadMST, debugFile);
                // graph.printList(ListHeadEdge)
                debugFile.write("Printing out contents of List Head Edge Linked List");
                graph.printList(graph.listHeadEdge, debugFile);
            } catch (Exception e){

            }
            


        }
        

        System.out.println("Finished moving edges and about to close files");

        try{
            // printList(listHeadMst, mstFile)
            mstFile.write("Final Linked List Representing MST of graph\n");
            graph.printList(graph.listHeadMST, mstFile);
            mstFile.write("\nFinal Cost of MST " + graph.totalMSTCost);

        } catch (Exception e){

        }

        

        try{
            inFile.close();
            debugFile.close();
            mstFile.close();


            System.out.println("closing files");

        } catch(Exception e){

        }
        
    }
}

