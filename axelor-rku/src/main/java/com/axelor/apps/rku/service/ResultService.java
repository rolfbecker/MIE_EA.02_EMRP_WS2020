package com.axelor.apps.rku.service;

import com.axelor.apps.rku.db.Result;
import com.axelor.apps.rku.db.ResultLine;
import java.util.List;

public interface ResultService {

  List<ResultLine> getResultLine(Result result);
}
