/**
 * File Name  : HrtmJobLevelDao.java
 * Package    : vn.com.vng.mcrusprofile.repository
 * Author     : 
 * Created    : Mar 30, 2022 14:58:10
 * This source code is generate by maven plugin. Not override if exists

Copyright © 2021 - 2022 by VNG
-------------------------------------------------------------------
 * Version Control:
 *       $LastChangedRevision$
 *       $LastChangedBy$
 *       $LastChangedDate$
 * 
-------------------------------------------------------------------
 */
package vn.com.vng.mcrusprofile.repository;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import vn.com.vng.mcrusprofile.domain.HrtmJobLevelTrain;

import java.util.List;

public interface HrtmJobLevelTrainDao extends GenericDAO<HrtmJobLevelTrain, Long> {
	
	HrtmJobLevelTrain getById(Long id);
	
	HrtmJobLevelTrain getByAggId (String aggId);
	
	HrtmJobLevelTrain getByCode(String code, String tenantCode);
	
	List<HrtmJobLevelTrain> getAll();
	
	List<HrtmJobLevelTrain> getAllActive(String tenantCode);

}