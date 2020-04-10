package smoothsail.domain.service

import org.junit.Test
import org.mockito.Mockito
import smoothsail.domain.BuildBranchDetails
import smoothsail.domain.BuildBranchStatus
import smoothsail.repository.BuildBranchDetailsRepository

internal class UpdateBuildBranchActionTest {
    private val buildBranchDetailsRepository = Mockito.mock(BuildBranchDetailsRepository::class.java)
    private val successUpdateBuildBranchAction = SuccessUpdateBuildBranchAction(buildBranchDetailsRepository)

    @Test
    fun `Entry properly updated on success`() {
        val original = BuildBranchDetails()
        successUpdateBuildBranchAction.execute(original, null, null)
        Mockito.verify(buildBranchDetailsRepository, Mockito.times(1)).save(original.copy(status = BuildBranchStatus.SUCCESS))
    }
}