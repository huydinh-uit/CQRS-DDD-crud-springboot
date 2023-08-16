/**
 * File Name  : HrtmJobLevelDaoImpl.java
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

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import vn.com.vng.core.server.genericdao.JPAGenericDAO;
import vn.com.vng.mcrusprofile.config.ComDatabaseConfiguration;
import vn.com.vng.mcrusprofile.domain.HrtmJobLevelTrain;
import vn.com.vng.mcrusprofile.domain.QHrtmJobLevelTrain;
import vn.com.vng.mcrusprofile.dto.JobLevelTrainDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class HrtmJobLevelDaoTrainImpl extends JPAGenericDAO<HrtmJobLevelTrain, Long> implements HrtmJobLevelTrainDao {
	@Override
	@PersistenceContext(unitName = ComDatabaseConfiguration.PERSISTENCE_UNIT)
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Override
	public HrtmJobLevelTrain getById(Long id) {
		JPQLQuery<HrtmJobLevelTrain> query = new JPAQuery<>(em());

		QHrtmJobLevelTrain hrtmJobLevelTrain = QHrtmJobLevelTrain.hrtmJobLevelTrain;
		return query.select(hrtmJobLevelTrain).from(hrtmJobLevelTrain).where(hrtmJobLevelTrain.id.eq(id)).fetchOne();
	}

	@Override
	public HrtmJobLevelTrain getByCode(String code, String tenantCode) {
		JPQLQuery<HrtmJobLevelTrain> query = new JPAQuery<>(em());

		QHrtmJobLevelTrain hrtmJobLevelTrain = QHrtmJobLevelTrain.hrtmJobLevelTrain;
		return query.select(hrtmJobLevelTrain).from(hrtmJobLevelTrain)
				.where(hrtmJobLevelTrain.code.eq(code).and(hrtmJobLevelTrain.tenantCode.eq(tenantCode))).fetchOne();
	}

	@Override
	public HrtmJobLevelTrain getByAggId(String aggId) {
		JPQLQuery<HrtmJobLevelTrain> query = new JPAQuery<>(em());

		QHrtmJobLevelTrain hrtmJobLevelTrain = QHrtmJobLevelTrain.hrtmJobLevelTrain;
		return query.select(hrtmJobLevelTrain).from(hrtmJobLevelTrain).where(hrtmJobLevelTrain.aggId.eq(aggId)).fetchOne();
	}
	
	@Override
	public List<HrtmJobLevelTrain> getAll() {
		JPQLQuery<HrtmJobLevelTrain> query = new JPAQuery<>(em());

		QHrtmJobLevelTrain hrtmJobLevelTrain = QHrtmJobLevelTrain.hrtmJobLevelTrain;
		return query.select(hrtmJobLevelTrain).from(hrtmJobLevelTrain)
				.fetch();
	}

	@Override
	public List<HrtmJobLevelTrain> getAllActive(String tenantCode) {
		JPQLQuery<HrtmJobLevelTrain> query = new JPAQuery<>(em());
		QHrtmJobLevelTrain hrtmJobLevelTrain = QHrtmJobLevelTrain.hrtmJobLevelTrain;
		return query.select(hrtmJobLevelTrain).from(hrtmJobLevelTrain)
//Question?				.where(hrtmJobLevelTrain.tenantCode.eq(tenantCode).and(hrtmJobLevelTrain.recordStatus.eq(RecordStatus.O.name())))
				.orderBy(hrtmJobLevelTrain.code.asc())
				.fetch();
	}

}