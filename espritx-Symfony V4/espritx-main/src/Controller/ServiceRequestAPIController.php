<?php

namespace App\Controller;

use App\Entity\Group;
use App\Entity\Service;
use App\Entity\ServiceRequest;
use App\Form\SerRequestType;
use App\Form\ServiceRequestType;
use App\Repository\ServiceRepository;
use App\Repository\ServiceRequestRepository;
use DateTimeImmutable;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\EntityManagerInterface;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\ParamConverter;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\SerializerInterface;
use Vich\UploaderBundle\Form\Type\VichFileType;

/**
 * @Route("/api/request")
 */
class ServiceRequestAPIController extends AbstractApiController
{
    /**
     * @Route("/showAll", name="app_service_request_api_all", methods={"GET"})
     */
    public function getRequests(ServiceRequestRepository $serviceRequestRepository)
    {
        $requests = $serviceRequestRepository->findAll();
        return $this->json($requests);
    }

    /**
     * @Route("/showUser", name="app_service_request_api_user", methods={"GET"})
     */
    public function getUserRequests(ServiceRequestRepository $serviceRequestRepository)
    {
        $requests = $serviceRequestRepository->findBy(["Requester" => $this->getUser()]);
        return $this->json($requests);
    }

    /**
     * @Route("/{id}/showService", name="app_service_request_api_service", methods={"GET"})
     */
    public function getServiceRequests(ServiceRequestRepository $serviceRequestRepository, Request $request, EntityManagerInterface $entityManager, $id)
    {
        $ser = $entityManager->getRepository(Service::class)->find($id);
        $requests = $serviceRequestRepository->findBy(["Type" => $ser]);
        return $this->json($requests);
    }

    /**
     * @Route("/showGroup", name="app_service_request_api_group", methods={"GET"})
     */
    public function getGroupRequests(ServiceRepository $serviceRepository)
    {
        $requests = new ArrayCollection();
        $UserGroup = $this->getUser()->getGroups();
        for ($i = 0; $i < count($UserGroup); $i++) {
            $services = $serviceRepository->findBy(["Responsible" => $this->getUser()->getGroups()[$i]]);
            for ($j = 0; $j < count($services); $j++) {
                $serequests = ($services[$j]->getServiceRequests());
                for ($k = 0; $k < count($serequests); $k++) {
                    $requests->add($serequests[$k]);
                }
            }
        }
        return $this->json($requests);
    }

    /**
     * @Route("/{id}", name="app_service_request_api_one", methods={"GET"})
     */
    public function getRequest($id, ServiceRequestRepository $serviceRequestRepository)
    {
        $request = $serviceRequestRepository->findOneBy(["id" => $id]);
        return $this->json($request->getAttachements());
    }

    /**
     * @Route("/edit/{id}", name="app_service_request_api_edit", methods={"POST"})
     * @ParamConverter("id", class="App\Entity\ServiceRequest")
     */
    public function updateRequest(ServiceRequest $req, ServiceRepository $serviceRepository, Request $request, EntityManagerInterface $em)
    {
        try {
            $request->setMethod("PATCH");
            $request->request->replace(json_decode($request->request->get("Request"), true));
            $request->request->remove("Requester");
            $service = $serviceRepository->findOneBy(["Name" => $request->get("Type")])?->getId();
            $request->request->set("Type", $service);
            if ($request->files->has("PictureFile")) {
                /** @var UploadedFile $var */
                $var = $request->files->get("PictureFile");
                $request->files->replace([
                    "PictureFile" => ["file" => $var]
                ]);
            }
            if ($request->files->has("AttachementsFile")) {
                $var = $request->files->get("AttachementsFile");
                $request->files->replace([
                    "AttachementsFile" => ["file" => $var]
                ]);
            }
            $editForm = $this->get('form.factory')->createNamed('', SerRequestType::class, $req, [
                "method" => "PATCH",
            ]);
            $editForm->handleRequest($request);
            if (!$editForm->isSubmitted() || !$editForm->isValid()) {
                return $this->respond($editForm, Response::HTTP_BAD_REQUEST);
            }
            $req = $editForm->getData();
            $em->flush();
            return $this->json($req);
        } catch (\Exception $e) {
            var_dump($e);
        }
    }

    /**
     * @Route("/{id}/delete", name="app_service_request_api_delete", methods={"DELETE"})
     */
    public function deleteRequest($id, ServiceRequestRepository $serviceRequestRepository): JsonResponse
    {
        $req = $serviceRequestRepository->findOneBy(['id' => $id]);

        $serviceRequestRepository->removeRequest($req);

        return new JsonResponse(['status' => 'Request deleted'], Response::HTTP_NO_CONTENT);
    }

    /**
     * @Route("/add", name="app_service_request_api_add", methods={"POST"})
     */
    public function add(ServiceRepository $serviceRepository, Request $request, EntityManagerInterface $em): JsonResponse
    {
        try {
            $req = new ServiceRequest();
            $request->request->replace(json_decode($request->request->get("Request"), true));
            $service = $serviceRepository->findOneBy(["Name" => $request->get("Type")])?->getId();
            $request->request->set("Type", $service);
            if ($request->files->has("PictureFile")) {
                $request->files->replace([
                    "PictureFile" => ["file" => $request->files->get("PictureFile")]
                ]);
            }
            if ($request->files->has("AttachementsFile")) {
                $request->files->replace([
                    "AttachementsFile" => ["file" => $request->files->get("AttachementsFile")]
                ]);
            }
            $editForm = $this->get('form.factory')->createNamed('', SerRequestType::class, $req, [
                "method" => "POST",
            ]);
            $editForm->handleRequest($request);
            if (!$editForm->isSubmitted() || !$editForm->isValid()) {
                return $this->respond($editForm, Response::HTTP_BAD_REQUEST);
            }
            $req = $editForm->getData();
            $req->setRequester($this->getUser());
            $em->persist($req);
            $em->flush();
            return $this->json([]);
        } catch (\Exception $e) {
            var_dump($e);
        }
    }

    /**
     * @Route("/respond/{id}", name="app_service_request_api_respond", methods={"PATCH"})
     * @ParamConverter("id", class="App\Entity\ServiceRequest")
     */
    public function respondRequest(ServiceRequest $req, ServiceRepository $serviceRepository, Request $request, EntityManagerInterface $em): JsonResponse
    {
        try {
            $request->request->remove("Requester");
            $editForm = $this->get('form.factory')->createNamed('', SerRequestType::class, $req, [
                "method" => "PATCH",
            ]);
            $editForm->add('RequestResponse', TextareaType::class);
            $editForm->add('Status', ChoiceType::class, [
                'choices' => [
                    'Hold' => 'processing',
                    'Deny' => 'denied',
                    'Done' => 'complete',
                ]
            ]);
            $editForm->remove('Title');
            $editForm->remove('PictureFile');
            $editForm->remove('AttachementsFile');
            $editForm->remove('Type');
            $editForm->handleRequest($request);
            if (!$editForm->isSubmitted() || !$editForm->isValid()) {
                return $this->respond($editForm, Response::HTTP_BAD_REQUEST);
            }
            $req = $editForm->getData();
            $req->setRespondedAt(new DateTimeImmutable());
          $em->flush();
            return $this->json($req);
        } catch (\Exception $e) {
            ;
        }
    }

    /**
     * @Route("/upload", name="app_service_request_api_respond", methods={"POST"})
     */
    public function RequestUpload(Request $request, FormBuilderInterface $builder): JsonResponse
    {
        $builder->add('AttachementsFile', FileType::class, [
            'allow_delete' => true,
            'delete_label' => "Delete?",
            'download_uri' => true,
        ]);

    }

}
