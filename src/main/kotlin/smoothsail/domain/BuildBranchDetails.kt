package smoothsail.domain

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class BuildBranchDetails(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val repository: String,

    @Column(nullable = false)
    val originBranch: String?,

    @Column(nullable = false)
    val targetBranch: String,

    @Column(nullable = false)
    val previousBuildBranchDetailsId: Long?,

    @Column(nullable = false)
    val currentBuildBranchName: String,

    @Column(nullable = false)
    val buildBranchHash: String,

    val createdAt: LocalDateTime,

    val status: BuildBranchStatus
)