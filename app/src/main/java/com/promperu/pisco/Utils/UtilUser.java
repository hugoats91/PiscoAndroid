package com.promperu.pisco.Utils;

import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.LocalService.Entity.EntityUser;

public class UtilUser {

    public static EntityUser getUser(){
        EntityUser user = AppDatabase.INSTANCE.userDao().getEntityUser();
        if(user!=null){
            return user;
        }else{
            return new EntityUser("", "", 0, 0, "",
                    0, 0, 0, 0, 0, "", 0, "", "", 0);
        }
    }
}
