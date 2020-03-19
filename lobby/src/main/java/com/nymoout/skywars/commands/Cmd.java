package com.nymoout.skywars.commands;


import com.nymoout.skywars.commands.BaseCmd;

public class Cmd extends BaseCmd {
	
	public Cmd() {
		forcePlayer = true;
		cmdName = "";
		alias = new String[]{"", ""};
		argLength = 2; //counting cmdName
	}

	@Override
	public boolean run() {
		return true;
	}

}
