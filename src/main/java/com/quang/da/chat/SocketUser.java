package com.quang.da.chat;

import java.security.Principal;
import java.util.Objects;

public class SocketUser implements Principal {
	private String name;
	private int id;
	private boolean isExpert;
	
	
	
	

    public boolean isExpert() {
		return isExpert;
	}

	public void setExpert(boolean isExpert) {
		this.isExpert = isExpert;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
    public String getName() {
        // TODO Auto-generated method stub
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof Principal))
            return false;

        Principal principal = (Principal) another;
        return principal.getName() == this.name;

    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
