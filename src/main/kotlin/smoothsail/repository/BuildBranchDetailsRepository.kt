package smoothsail.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus

@Repository
interface BuildBranchDetailsRepository: JpaRepository<BuildBranchDetails, Long> {
  fun findFirstByRepositoryAndTargetBranchAndStatusInOrderByCreatedAtDesc(
      repository: String,
      targetBranch: String,
      status: List<BuildBranchStatus>
  ): BuildBranchDetails?

  fun findFirstByRepositoryAndCurrentBuildBranchNameAndStatusInOrderByCreatedAtDesc(
      repository: String,
      currentBuildBranchName: String,
      status: List<BuildBranchStatus>
  ): BuildBranchDetails?
}