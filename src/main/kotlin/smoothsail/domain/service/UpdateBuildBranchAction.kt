package smoothsail.domain.service

import org.springframework.stereotype.Component
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus

@Component
class UpdateBuildBranchActionSelectionStrategy(
    private val successUpdateBuildBranchAction: SuccessUpdateBuildBranchAction,
    private val failureUpdateBuildBranchAction: FailureUpdateBuildBranchAction,
    private val nullUpdateBuildBranchAction: NullUpdateBuildBranchAction
) {

  fun select(updatedStatus: BuildBranchStatus) =
    when (updatedStatus) {
      BuildBranchStatus.SUCCESS -> successUpdateBuildBranchAction
      BuildBranchStatus.FAILURE -> failureUpdateBuildBranchAction
      else -> nullUpdateBuildBranchAction
    }
}

interface UpdateBuildBranchAction {
  fun execute(buildBranchDetails: BuildBranchDetails)
}

@Component
class SuccessUpdateBuildBranchAction: UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails) {
    // do nothing
  }
}

@Component
class FailureUpdateBuildBranchAction: UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails) {
    // do nothing
  }
}

@Component
class NullUpdateBuildBranchAction: UpdateBuildBranchAction {
  override fun execute(buildBranchDetails: BuildBranchDetails) {
    // do nothing
  }
}