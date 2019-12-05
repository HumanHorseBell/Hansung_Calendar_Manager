package com.humanhorsebell.computer.hansung_calendar_manager

data class Schedule(val grpName : String, val startDate : String, val endDate : String, val picture : String?, val name : String) : Comparable<Schedule>{
    override fun compareTo(other: Schedule): Int {
        /*for(i in 0..startDate.length){
            if(startDate[i] > other.startDate[i]) return 1;
            if(startDate[i] < other.startDate[i]) return -1;
        }*/

        if(startDate > other.startDate) return 1;
        if(startDate < other.startDate) return -1;
        if(name > other.name) return 1;
        if(name < other.name) return -1;
        else return 0;
    }
}
