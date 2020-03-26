/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.peterssond.hibernatetest;

import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ABootstrapBean {

    private final ARepository aRepo;

    @Autowired
    public ABootstrapBean(ARepository aRepo) {
        this.aRepo = aRepo;
    }

    @PostConstruct
    public void init() {

        aRepo.save(createA(true, UUID.randomUUID().toString()));
    }

    private EntityA createA(boolean includeB, String guid) {
        EntityA entityA = new EntityA();
        entityA.setAId(guid);

        if (includeB) {
            entityA.setEntityB(new EntityB(guid));
        }

        return entityA;
    }

}
