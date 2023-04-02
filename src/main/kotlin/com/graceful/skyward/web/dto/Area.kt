package com.graceful.skyward.web.dto

import org.springframework.data.relational.core.mapping.Column

data class Area(
        @Column("ps_start_x") val startX: Int,
        @Column("ps_start_y") val startY: Int,
        @Column("ps_start_Z") val startZ: Int,
        @Column("ps_final_x") val finalX: Int,
        @Column("ps_final_y") val finalY: Int,
        @Column("ps_final_z") val finalZ: Int
    )
