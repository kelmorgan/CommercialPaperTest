package com.cp.utils;

import com.newgen.iforms.custom.IFormReference;

public interface SharedI {
     void cpSendMail(IFormReference ifr);
     void cpFormLoadActivity(IFormReference ifr);
     void cpSetDecision(IFormReference ifr);
}
