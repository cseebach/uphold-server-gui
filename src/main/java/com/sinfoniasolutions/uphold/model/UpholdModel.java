package com.sinfoniasolutions.uphold.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron Seebach on 12/26/13.
 */
public class UpholdModel {

    public List<String> getComputers(){
        ArrayList<String> computers = new ArrayList<String>();
        computers.add("SEEBACHWS36");
        computers.add("SEEBACHWS37");
        computers.add("SEEBACHWS38");
        computers.add("SEEBACHWS39");
        computers.add("SEEBACHWS40");
        computers.add("SEEBACHWS41");
        return computers;
    }

    public ComputerDetails getDetails(String computer){
        return new ComputerDetails();
    }

}
