package smoothsail.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class JobBuildDetails(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0,

    @Column(nullable = false)
    val buildBranchDetailsId: Long = 0,

    @Column(nullable = false)
    val jobName: String = "",

    @Column(nullable = false)
    val buildNumber: Long = 0
)